package medconnect.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

/** Testes de integração da API (banco H2 em memória, sem MySQL). */
@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    private MockHttpSession logar() throws Exception {
        MockHttpSession s = new MockHttpSession();
        mvc.perform(post("/api/login").session(s).contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@medconnect.com\",\"senha\":\"admin123\"}")).andExpect(status().isOk());
        return s;
    }

    private String paciente(String cpf) {
        return "{\"nome\":\"Maria da Silva\",\"cpf\":\"" + cpf + "\",\"nascimento\":\"1990-05-20\","
                + "\"telefone\":\"(13) 99999-8888\",\"email\":\"maria@email.com\",\"endereco\":\"Rua A\"}";
    }

    private int criar(MockHttpSession s, String url, String json) throws Exception {
        String corpo = mvc.perform(post(url).session(s).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return JsonPath.read(corpo, "$.id");
    }

    @Test
    @DisplayName("API sem login retorna 401")
    void semLogin() throws Exception {
        mvc.perform(get("/api/pacientes")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login com senha errada retorna 401")
    void senhaErrada() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@medconnect.com\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.erro").value("E-mail ou senha incorretos"));
    }

    @Test
    @DisplayName("Cadastra paciente e bloqueia CPF duplicado")
    void cpfDuplicado() throws Exception {
        MockHttpSession s = logar();
        criar(s, "/api/pacientes", paciente("529.982.247-25"));
        mvc.perform(post("/api/pacientes").session(s).contentType(MediaType.APPLICATION_JSON)
                .content(paciente("52998224725").replace("Maria da Silva", "Outro Nome")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erro").value("CPF já cadastrado"));
    }

    @Test
    @DisplayName("CPF inválido retorna 400")
    void cpfInvalido() throws Exception {
        mvc.perform(post("/api/pacientes").session(logar()).contentType(MediaType.APPLICATION_JSON)
                .content(paciente("111.111.111-11")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erro").value("CPF inválido"));
    }

    @Test
    @DisplayName("Nome vazio retorna mensagem de validação")
    void nomeVazio() throws Exception {
        mvc.perform(post("/api/pacientes").session(logar()).contentType(MediaType.APPLICATION_JSON)
                .content(paciente("390.533.447-05").replace("Maria da Silva", "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Agenda: bloqueia conflito de horário e data no passado")
    void agenda() throws Exception {
        MockHttpSession s = logar();
        int idPac = criar(s, "/api/pacientes", paciente("390.533.447-05"));
        int idMed = criar(s, "/api/medicos",
                "{\"nome\":\"Dr. Carlos Souza\",\"crm\":\"12345\",\"especialidade\":\"Cardiologia\",\"horario\":\"08h-17h\"}");
        String quando = LocalDateTime.now().plusDays(5).withSecond(0).withNano(0).toString();
        String json = "{\"paciente\":{\"id\":" + idPac + "},\"medico\":{\"id\":" + idMed + "},\"dataHora\":\"" + quando
                + "\",\"motivo\":\"Rotina\"}";
        criar(s, "/api/consultas", json);
        mvc.perform(post("/api/consultas").session(s).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erro").value("Horário já agendado para este médico"));
        String passado = LocalDateTime.now().minusDays(1).withSecond(0).withNano(0).toString();
        mvc.perform(post("/api/consultas").session(s).contentType(MediaType.APPLICATION_JSON)
                .content(json.replace(quando, passado)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erro").value("Não é possível agendar consultas no passado"));
        mvc.perform(get("/api/dashboard").session(s)).andExpect(status().isOk()).andExpect(jsonPath("$.consultasFuturas").exists());
    }

    @Test
    @DisplayName("CRM inválido e CRM duplicado retornam 400")
    void crm() throws Exception {
        MockHttpSession s = logar();
        String med = "{\"nome\":\"Dra. Ana Lima\",\"crm\":\"%s\",\"especialidade\":\"Pediatria\",\"horario\":\"08h-12h\"}";
        mvc.perform(post("/api/medicos").session(s).contentType(MediaType.APPLICATION_JSON).content(med.formatted("123")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erro").value("CRM deve ter entre 4 e 7 dígitos"));
        criar(s, "/api/medicos", med.formatted("777777"));
        mvc.perform(post("/api/medicos").session(s).contentType(MediaType.APPLICATION_JSON).content(med.formatted("777777")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erro").value("CRM já cadastrado"));
    }
}
