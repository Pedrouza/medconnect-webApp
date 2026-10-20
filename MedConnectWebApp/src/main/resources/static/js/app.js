(() => {
  const $ = (s, r = document) => r.querySelector(s), $$ = (s, r = document) => [...r.querySelectorAll(s)];
  const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
  const fmt = v => /^\d{4}-\d\d-\d\dT/.test(v) ? new Date(v).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' })
    : /^\d{4}-\d\d-\d\d$/.test(v) ? v.split('-').reverse().join('/') : v;
  const get = (o, p) => p.split('.').reduce((a, k) => a?.[k], o);
  const setp = (o, p, v) => { const ks = p.split('.'); ks.slice(0, -1).reduce((a, k) => a[k] ??= {}, o)[ks[ks.length - 1]] = v; };
  const NAV = [['dashboard.html', 'Dashboard'], ['pacientes.html', 'Pacientes'], ['medicos.html', 'Médicos'], ['agenda.html', 'Agenda'], ['prontuarios.html', 'Prontuários']];
  const naLogin = () => /(^\/$|index\.html$)/.test(location.pathname);

  // Comunicação com o back-end (REST/JSON). 401 fora do login => volta para a tela de login.
  async function api(url, metodo = 'GET', corpo) {
    const r = await fetch(url, { method: metodo, headers: { 'Content-Type': 'application/json' }, body: corpo ? JSON.stringify(corpo) : undefined });
    if (r.status === 401 && !naLogin()) { location.href = 'index.html'; throw new Error('Sessão expirada'); }
    const t = await r.text(), d = t ? JSON.parse(t) : null;
    if (!r.ok) throw new Error(d?.erro || 'Erro ' + r.status);
    return d;
  }

  function checar(campo) {
    const m = Validacoes.validar(campo.value, campo.dataset.rule);
    campo.classList.toggle('invalido', !!m);
    const e = campo.parentElement.querySelector('.erro'); if (e) e.textContent = m;
    return !m;
  }
  const validarForm = f => $$('[data-rule]', f).map(checar).every(Boolean);
  function mascaras(f) {
    $$('[data-mask]', f).forEach(i => i.addEventListener('input', () => {
      const d = i.value.replace(/\D/g, '');
      if (i.dataset.mask === 'cpf') i.value = d.slice(0, 11).replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1,2})$/, '$1-$2');
      else i.value = d.slice(0, 11).replace(/(\d{2})(\d)/, '($1) $2').replace(/(\d{4,5})(\d{4})$/, '$1-$2');
    }));
    $$('[data-rule]', f).forEach(c => c.addEventListener('blur', () => checar(c)));
  }

  // Menu lateral (páginas internas exigem sessão no servidor)
  if (document.body.classList.contains('app')) {
    const pg = location.pathname.split('/').pop();
    document.body.insertAdjacentHTML('afterbegin', `<aside class="side"><h2>MedConnect</h2>${NAV.map(([h, t]) => `<a href="${h}" class="${h === pg ? 'on' : ''}">${t}</a>`).join('')}<a href="#" id="sair">Sair</a></aside>`);
    $('#sair').onclick = async e => { e.preventDefault(); try { await api('/api/logout', 'POST'); } catch (x) {} location.href = 'index.html'; };
    api('/api/me').catch(() => {});
  }

  // Login
  const login = $('#login');
  if (login) {
    mascaras(login);
    login.addEventListener('submit', async e => {
      e.preventDefault();
      const msg = (t) => { $('#msg').textContent = t; $('#msg').className = 'msg no'; };
      if (!validarForm(login)) return msg('Corrija os campos destacados.');
      try { await api('/api/login', 'POST', { email: $('#email').value, senha: $('#senha').value }); location.href = 'dashboard.html'; }
      catch (x) { msg(x.message); }
    });
  }

  // Dashboard
  if ($('#nPac')) {
    api('/api/dashboard').then(d => {
      $('#nPac').textContent = d.pacientes; $('#nMed').textContent = d.medicos; $('#nCon').textContent = d.consultasFuturas;
      $('#proximas').innerHTML = d.proximas.map(c => `<tr><td>${esc(fmt(c.dataHora))}</td><td>${esc(c.paciente.nome)}</td><td>${esc(c.medico.nome)}</td></tr>`).join('') || '<tr><td colspan="3">Nenhuma consulta futura.</td></tr>';
    }).catch(() => {});
  }

  // CRUD genérico sobre a API REST (/api/<store>)
  $$('form[data-store]').forEach(f => {
    const k = f.dataset.store, tab = $('#tabela'), cols = tab.dataset.cols.split(','); let editId = null, dados = [];
    mascaras(f);
    const msg = (t, ok) => { $('#msg').textContent = t; $('#msg').className = 'msg ' + (ok ? 'ok' : 'no'); };
    const rotulo = (src, i) => src === 'consultas' ? `${i.paciente.nome} - ${fmt(i.dataHora)}` : i.nome;
    const selects = Promise.all($$('select[data-source]', f).map(async s => {
      const lista = await api('/api/' + s.dataset.source);
      s.innerHTML = '<option value="">Selecione...</option>' + lista.map(i => `<option value="${i.id}">${esc(rotulo(s.dataset.source, i))}</option>`).join('');
    })).catch(x => msg(x.message, false));

    async function desenhar() {
      try { dados = await api('/api/' + k); } catch (x) { return msg(x.message, false); }
      const q = ($('#busca')?.value || '').toLowerCase();
      $('tbody', tab).innerHTML = dados.filter(o => cols.some(c => String(get(o, c) ?? '').toLowerCase().includes(q)))
        .map(o => `<tr>${cols.map(c => `<td>${esc(fmt(get(o, c)))}</td>`).join('')}<td><button data-e="${o.id}" class="sec">Editar</button><button data-d="${o.id}" class="del">Excluir</button></td></tr>`).join('')
        || `<tr><td colspan="${cols.length + 1}">Nenhum registro.</td></tr>`;
    }

    f.addEventListener('submit', async e => {
      e.preventDefault();
      if (!validarForm(f)) return msg('Corrija os campos destacados.', false);
      const o = {};
      $$('[name]', f).forEach(c => setp(o, c.name, c.name.endsWith('.id') ? Number(c.value) : c.value.trim()));
      try {
        await (editId ? api(`/api/${k}/${editId}`, 'PUT', o) : api('/api/' + k, 'POST', o));
        msg(editId ? 'Registro atualizado!' : 'Registro salvo com sucesso!', true); editId = null; f.reset(); desenhar();
      } catch (x) { msg(x.message, false); }
    });

    tab.addEventListener('click', async e => {
      const b = e.target.closest('button'); if (!b) return;
      if (b.dataset.d && confirm('Excluir este registro?')) {
        try { await api(`/api/${k}/${b.dataset.d}`, 'DELETE'); msg('Registro excluído.', true); desenhar(); } catch (x) { msg(x.message, false); }
      }
      if (b.dataset.e) {
        await selects; editId = b.dataset.e; const item = dados.find(o => String(o.id) === editId);
        $$('[name]', f).forEach(c => { let v = get(item, c.name) ?? ''; if (c.type === 'datetime-local') v = String(v).slice(0, 16); c.value = v; });
        msg('Editando registro: altere e salve.', true); window.scrollTo(0, 0);
      }
    });
    $('#busca')?.addEventListener('input', desenhar);
    $('#limpar')?.addEventListener('click', () => { editId = null; $('#msg').textContent = ''; $$('.erro', f).forEach(x => x.textContent = ''); });
    desenhar();
  });
})();
