// Regras de validação (equivalentes às do projeto Java: validators)
const Validacoes = (() => {
  const dig = v => v.replace(/\D/g, '');
  function cpfOk(v) {
    const c = dig(v);
    if (c.length !== 11 || /^(\d)\1{10}$/.test(c)) return false;
    const dv = n => { let s = 0; for (let i = 0; i < n; i++) s += c[i] * (n + 1 - i); const r = (s * 10) % 11; return r === 10 ? 0 : r; };
    return dv(9) == c[9] && dv(10) == c[10];
  }
  const regras = {
    required: v => v.trim() ? '' : 'Campo obrigatório',
    min3: v => v.trim().length >= 3 ? '' : 'Mínimo de 3 caracteres',
    max100: v => v.length <= 100 ? '' : 'Máximo de 100 caracteres',
    max500: v => v.length <= 500 ? '' : 'Máximo de 500 caracteres',
    email: v => /^[\w.+-]+@[\w-]+(\.[\w-]+)+$/.test(v) ? '' : 'E-mail inválido',
    senha: v => v.length >= 6 ? '' : 'A senha deve ter no mínimo 6 caracteres',
    tel: v => /^\d{10,11}$/.test(dig(v)) ? '' : 'Telefone inválido (10 ou 11 dígitos)',
    cpf: v => cpfOk(v) ? '' : 'CPF inválido',
    crm: v => /^\d{4,7}$/.test(dig(v)) ? '' : 'CRM deve ter entre 4 e 7 dígitos',
    nasc: v => { const d = new Date(v), h = new Date();
      if (d > h) return 'Data de nascimento não pode ser no futuro';
      return h.getFullYear() - d.getFullYear() > 120 ? 'Idade superior a 120 anos' : ''; },
    futuro: v => new Date(v) > new Date() ? '' : 'Não é possível agendar no passado'
  };
  function validar(valor, lista) {
    for (const r of lista.split(',')) { const m = regras[r.trim()](valor); if (m) return m; }
    return '';
  }
  return { validar, cpfOk };
})();
