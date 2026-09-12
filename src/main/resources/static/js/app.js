const apiUrl = '/api/pontos';
const statusElement = document.getElementById('status');
const totalPointsElement = document.getElementById('total-pontos');
const mapMessageElement = document.getElementById('map-message');
const cadastroModal = document.getElementById('cadastro-modal');
const cadastroForm = document.getElementById('cadastro-form');
const formStatusElement = document.getElementById('form-status');
const buscaElement = document.getElementById('busca-ponto');
const filtroTipoElement = document.getElementById('filtro-tipo');
const filtroDisponibilidadeElement = document.getElementById('filtro-disponibilidade');
const limparFiltrosElement = document.getElementById('limpar-filtros');
const dashboardBuscaElement = document.getElementById('dashboard-busca');
const dashboardStatusElement = document.getElementById('dashboard-status');
const dashboardLimparElement = document.getElementById('dashboard-limpar');
const dashboardTabelaElement = document.getElementById('dashboard-tabela');
const dashboardMensagemElement = document.getElementById('dashboard-status-mensagem');
const dashboardTotalElement = document.getElementById('dashboard-total');
const dashboardPendentesElement = document.getElementById('dashboard-pendentes');
const dashboardAprovadosElement = document.getElementById('dashboard-aprovados');
const dashboardRejeitadosElement = document.getElementById('dashboard-rejeitados');
const saoPauloCenter = [-23.5505, -46.6333];
const map = L.map('map').setView(saoPauloCenter, 11);
const markerLayer = L.layerGroup().addTo(map);
let pontosApi = [];
let buscaTimeout;
let dashboardBuscaTimeout;

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

const markerColors = {
  GRATUITA: '#2e9d68',
  COMPRA: '#2779bd',
  DISTRIBUICAO: '#d8972e',
  NECESSIDADE: '#c44d43'
};

function atualizarTamanhoMapa() {
  map.invalidateSize({
    pan: false,
    debounceMoveend: true
  });
}

requestAnimationFrame(atualizarTamanhoMapa);
window.addEventListener('load', atualizarTamanhoMapa);
window.addEventListener('resize', atualizarTamanhoMapa);

document.getElementById('cadastrar-ponto').addEventListener('click', abrirCadastro);
document.querySelectorAll('[data-fechar-modal]').forEach((elemento) => {
  elemento.addEventListener('click', fecharCadastro);
});
cadastroForm.addEventListener('submit', enviarCadastro);
buscaElement.addEventListener('input', () => {
  window.clearTimeout(buscaTimeout);
  buscaTimeout = window.setTimeout(aplicarFiltros, 250);
});
filtroTipoElement.addEventListener('change', aplicarFiltros);
filtroDisponibilidadeElement.addEventListener('change', aplicarFiltros);
limparFiltrosElement.addEventListener('click', limparFiltros);
dashboardBuscaElement.addEventListener('input', () => {
  window.clearTimeout(dashboardBuscaTimeout);
  dashboardBuscaTimeout = window.setTimeout(renderizarDashboard, 250);
});
dashboardStatusElement.addEventListener('change', renderizarDashboard);
dashboardLimparElement.addEventListener('click', limparFiltrosDashboard);

function abrirCadastro() {
  formStatusElement.textContent = '';
  formStatusElement.classList.remove('success');
  cadastroModal.hidden = false;
  cadastroForm.elements.nome.focus();
}

function fecharCadastro() {
  cadastroModal.hidden = true;
}

async function enviarCadastro(evento) {
  evento.preventDefault();
  formStatusElement.classList.remove('success');

  const dados = new FormData(cadastroForm);
  const payload = {
    nome: dados.get('nome').trim(),
    descricao: dados.get('descricao').trim() || null,
    tipo: dados.get('tipo'),
    endereco: dados.get('endereco').trim(),
    latitude: Number(dados.get('latitude')),
    longitude: Number(dados.get('longitude')),
    horarioInicio: dados.get('horarioInicio'),
    horarioFim: dados.get('horarioFim'),
    disponibilidade: dados.get('disponibilidade'),
    observacao: dados.get('observacao').trim() || null
  };

  const erro = validarCadastro(payload);
  if (erro) {
    formStatusElement.textContent = erro;
    return;
  }

  formStatusElement.textContent = 'Enviando cadastro...';

  try {
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const mensagem = await obterMensagemErro(response);
      throw new Error(mensagem);
    }

    cadastroForm.reset();
    fecharCadastro();
    statusElement.classList.remove('error');
    statusElement.textContent = 'Ponto cadastrado com sucesso. Aguardando aprovação.';
    await carregarPontos();
  } catch (error) {
    console.error(error);
    formStatusElement.textContent = error.message || 'Não foi possível cadastrar o ponto.';
  }
}

function validarCadastro(payload) {
  const camposObrigatorios = [
    ['nome', 'Nome'],
    ['tipo', 'Tipo'],
    ['endereco', 'Endereço'],
    ['horarioInicio', 'Horário inicial'],
    ['horarioFim', 'Horário final'],
    ['disponibilidade', 'Disponibilidade']
  ];

  for (const [campo, nome] of camposObrigatorios) {
    if (!payload[campo]) {
      return `${nome} é obrigatório.`;
    }
  }

  if (!Number.isFinite(payload.latitude) || payload.latitude < -90 || payload.latitude > 90) {
    return 'Latitude deve estar entre -90 e 90.';
  }

  if (!Number.isFinite(payload.longitude) || payload.longitude < -180 || payload.longitude > 180) {
    return 'Longitude deve estar entre -180 e 180.';
  }

  if (payload.horarioFim < payload.horarioInicio) {
    return 'Horário final não pode ser anterior ao horário inicial.';
  }

  return '';
}

async function obterMensagemErro(response) {
  try {
    const corpo = await response.json();
    return corpo.message || corpo.error || `Não foi possível cadastrar o ponto (${response.status}).`;
  } catch (error) {
    return `Não foi possível cadastrar o ponto (${response.status}).`;
  }
}

async function carregarPontos() {
  statusElement.textContent = 'Carregando pontos...';

  try {
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Erro ao carregar pontos: ${response.status}`);
    }

    const pontos = await response.json();
    pontosApi = Array.isArray(pontos) ? pontos : [];
    atualizarIndicadoresDashboard(pontosApi);
    renderizarDashboard();

    if (pontosApi.length === 0) {
      markerLayer.clearLayers();
      totalPointsElement.textContent = '0 pontos';
      statusElement.textContent = 'Nenhum ponto encontrado.';
      mostrarMensagem('Nenhum ponto de água cadastrado na API.');
      atualizarTamanhoMapa();
      return;
    }

    aplicarFiltros();
  } catch (error) {
    console.error(error);
    pontosApi = [];
    atualizarIndicadoresDashboard(pontosApi);
    renderizarDashboard();
    markerLayer.clearLayers();
    statusElement.textContent = 'Não foi possível carregar os pontos da API.';
    statusElement.classList.add('error');
    mostrarMensagem('Verifique a comunicação com o backend e tente novamente.');
    totalPointsElement.textContent = '0 pontos';
    atualizarTamanhoMapa();
  }
}

function atualizarIndicadoresDashboard(pontos) {
  dashboardTotalElement.textContent = pontos.length;
  dashboardPendentesElement.textContent = pontos.filter((ponto) => ponto.statusAprovacao === 'PENDENTE').length;
  dashboardAprovadosElement.textContent = pontos.filter((ponto) => ponto.statusAprovacao === 'APROVADO').length;
  dashboardRejeitadosElement.textContent = pontos.filter((ponto) => ponto.statusAprovacao === 'REJEITADO').length;
}

function renderizarDashboard() {
  const termo = dashboardBuscaElement.value.trim().toLocaleLowerCase('pt-BR');
  const statusSelecionado = dashboardStatusElement.value;
  const resultados = pontosApi.filter((ponto) => {
    const nome = String(ponto.nome || '').toLocaleLowerCase('pt-BR');
    const endereco = String(ponto.endereco || '').toLocaleLowerCase('pt-BR');
    const correspondeBusca = !termo || nome.includes(termo) || endereco.includes(termo);
    const correspondeStatus = statusSelecionado === 'TODOS' || ponto.statusAprovacao === statusSelecionado;
    return correspondeBusca && correspondeStatus;
  });

  dashboardTabelaElement.innerHTML = resultados.length > 0
    ? resultados.map(criarLinhaDashboard).join('')
    : '<tr><td class="table-empty" colspan="6">Nenhum ponto de água encontrado.</td></tr>';

  dashboardMensagemElement.textContent = resultados.length > 0
    ? `${resultados.length} ponto${resultados.length === 1 ? '' : 's'} encontrado${resultados.length === 1 ? '' : 's'}.`
    : 'Nenhum ponto de água encontrado.';
}

function criarLinhaDashboard(ponto) {
  const status = ponto.statusAprovacao || 'NÃO INFORMADO';
  const classeStatus = status.toLocaleLowerCase('pt-BR');

  return `
    <tr>
      <td data-label="Nome">${escaparHtml(ponto.nome || 'Sem nome')}</td>
      <td data-label="Tipo">${escaparHtml(ponto.tipo || 'Não informado')}</td>
      <td data-label="Endereço">${escaparHtml(ponto.endereco || 'Não informado')}</td>
      <td data-label="Disponibilidade">${escaparHtml(ponto.disponibilidade || 'Não informado')}</td>
      <td data-label="Status de aprovação"><span class="status-pill ${escaparHtml(classeStatus)}">${escaparHtml(status)}</span></td>
      <td data-label="Data de cadastro">${formatarDataCadastro(ponto.dataCadastro)}</td>
    </tr>
  `;
}

function limparFiltrosDashboard() {
  window.clearTimeout(dashboardBuscaTimeout);
  dashboardBuscaElement.value = '';
  dashboardStatusElement.value = 'TODOS';
  renderizarDashboard();
}

function formatarDataCadastro(valor) {
  if (!valor) {
    return 'Não informada';
  }

  const data = new Date(valor);
  return Number.isNaN(data.getTime()) ? escaparHtml(valor) : data.toLocaleString('pt-BR');
}

function aplicarFiltros() {
  const termo = buscaElement.value.trim().toLocaleLowerCase('pt-BR');
  const tipoSelecionado = filtroTipoElement.value;
  const disponibilidadeSelecionada = filtroDisponibilidadeElement.value;
  const pontosAprovados = pontosApi
      .filter((ponto) => ponto.statusAprovacao === 'APROVADO')
      .filter((ponto) => possuiLocalizacaoValida(ponto));
  const pontosFiltrados = pontosAprovados.filter((ponto) => {
    const nome = String(ponto.nome || '').toLocaleLowerCase('pt-BR');
    const endereco = String(ponto.endereco || '').toLocaleLowerCase('pt-BR');
    const correspondeBusca = !termo || nome.includes(termo) || endereco.includes(termo);
    const correspondeTipo = tipoSelecionado === 'TODOS' || ponto.tipo === tipoSelecionado;
    const correspondeDisponibilidade = disponibilidadeSelecionada === 'TODAS'
      || ponto.disponibilidade === disponibilidadeSelecionada;

    return correspondeBusca && correspondeTipo && correspondeDisponibilidade;
  });

  markerLayer.clearLayers();
  pontosFiltrados.forEach(adicionarMarcador);
  totalPointsElement.textContent = `${pontosFiltrados.length} ponto${pontosFiltrados.length === 1 ? '' : 's'}`;
  esconderMensagem();

  if (pontosFiltrados.length === 0) {
    statusElement.textContent = 'Nenhum ponto de água encontrado.';
    mostrarMensagem('Nenhum ponto de água encontrado.');
    atualizarTamanhoMapa();
    return;
  }

  statusElement.classList.remove('error');
  statusElement.textContent = 'Pontos aprovados carregados com sucesso.';
  ajustarMapa(pontosFiltrados);
  atualizarTamanhoMapa();
}

function limparFiltros() {
  window.clearTimeout(buscaTimeout);
  buscaElement.value = '';
  filtroTipoElement.value = 'TODOS';
  filtroDisponibilidadeElement.value = 'TODAS';
  aplicarFiltros();
}

function possuiLocalizacaoValida(ponto) {
  return Number.isFinite(Number(ponto.latitude))
    && Number.isFinite(Number(ponto.longitude))
    && Number(ponto.latitude) >= -90
    && Number(ponto.latitude) <= 90
    && Number(ponto.longitude) >= -180
    && Number(ponto.longitude) <= 180;
}

function adicionarMarcador(ponto) {
  const tipo = ponto.tipo || 'NÃO INFORMADO';
  const cor = markerColors[tipo] || '#0c6b68';
  const icone = L.divIcon({
    className: 'water-marker-wrapper',
    html: `<span class="water-marker" style="--marker-color: ${cor}"></span>`,
    iconSize: [24, 34],
    iconAnchor: [12, 34],
    popupAnchor: [0, -32]
  });

  L.marker([Number(ponto.latitude), Number(ponto.longitude)], { icon: icone })
    .addTo(markerLayer)
    .bindPopup(criarPopup(ponto));
}

function criarPopup(ponto) {
  return `
    <div class="popup-content">
      <h3>${escaparHtml(ponto.nome || 'Ponto sem nome')}</h3>
      <p><strong>Tipo:</strong> ${escaparHtml(ponto.tipo || 'Não informado')}</p>
      <p><strong>Endereço:</strong> ${escaparHtml(ponto.endereco || 'Não informado')}</p>
      <p><strong>Horário:</strong> ${formatarHorario(ponto.horarioInicio)} - ${formatarHorario(ponto.horarioFim)}</p>
      <p><strong>Disponibilidade:</strong> ${escaparHtml(ponto.disponibilidade || 'Não informado')}</p>
      <p><strong>Observação:</strong> ${escaparHtml(ponto.observacao || 'Nenhuma observação')}</p>
    </div>
  `;
}

function ajustarMapa(pontos) {
  if (pontos.length === 1) {
    map.setView([Number(pontos[0].latitude), Number(pontos[0].longitude)], 15, { animate: false });
    return;
  }

  const limites = L.latLngBounds(pontos.map((ponto) => [Number(ponto.latitude), Number(ponto.longitude)]));
  map.fitBounds(limites, { padding: [36, 36], maxZoom: 15, animate: false });
}

function mostrarMensagem(mensagem) {
  mapMessageElement.textContent = mensagem;
  mapMessageElement.classList.add('visible');
}

function esconderMensagem() {
  mapMessageElement.textContent = '';
  mapMessageElement.classList.remove('visible');
}

function escaparHtml(valor) {
  return String(valor)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}

function formatarHorario(valor) {
  if (!valor) {
    return 'Não informado';
  }

  if (typeof valor === 'string') {
    return valor;
  }

  return String(valor);
}

window.addEventListener('DOMContentLoaded', carregarPontos);
