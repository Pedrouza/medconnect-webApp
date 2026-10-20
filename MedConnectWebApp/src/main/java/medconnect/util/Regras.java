package medconnect.util;

/** Regras de validação de formato (CPF, telefone, CRM), portadas dos validators do projeto desktop. */
public final class Regras {

    private Regras() {}

    public static String digitos(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }

    public static boolean cpfValido(String cpf) {
        String c = digitos(cpf);
        if (c.length() != 11 || c.matches("(\\d)\\1{10}")) return false;
        return dv(c, 9) == c.charAt(9) - '0' && dv(c, 10) == c.charAt(10) - '0';
    }

    private static int dv(String c, int n) {
        int soma = 0;
        for (int i = 0; i < n; i++) soma += (c.charAt(i) - '0') * (n + 1 - i);
        int r = (soma * 10) % 11;
        return r == 10 ? 0 : r;
    }

    /** Formata 11 dígitos como 000.000.000-00 (o CPF deve ter sido validado antes). */
    public static String formatarCpf(String cpf) {
        String c = digitos(cpf);
        return c.substring(0, 3) + "." + c.substring(3, 6) + "." + c.substring(6, 9) + "-" + c.substring(9);
    }

    public static boolean telefoneValido(String t) {
        int n = digitos(t).length();
        return n == 10 || n == 11;
    }

    public static boolean crmValido(String crm) {
        int n = digitos(crm).length();
        return n >= 4 && n <= 7;
    }
}
