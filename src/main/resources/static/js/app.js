const apiUrl = '/api/pontos';
const statusElement = document.getElementById('status');
const totalPointsElement = document.getElementById('total-pontos');
const mapMessageElement = document.getElementById('map-message');
const saoPauloCenter = [-23.5505, -46.6333];
const map = L.map('map').setView(saoPauloCenter, 11);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

const markerColors = {
  GRATUITA: '#2e9d68',
  COMPRA: '#2779bd',
  DISTRIBUICAO: '#d8972e'
};

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

    if (!Array.isArray(pontos) || pontos.length === 0) {
      totalPointsElement.textContent = '0 pontos';
      statusElement.textContent = 'Nenhum ponto encontrado.';
      mostrarMensagem('Nenhum ponto de água cadastrado na API.');
      return;
    }

    const pontosAprovados = pontos.filter((ponto) => ponto.statusAprovacao === 'APROVADO');
    const pontosValidos = pontosAprovados.filter((ponto) => possuiLocalizacaoValida(ponto));
    const pontosInvalidos = pontosAprovados.length - pontosValidos.length;

    pontosValidos.forEach(adicionarMarcador);
    totalPointsElement.textContent = `${pontosValidos.length} ponto${pontosValidos.length === 1 ? '' : 's'}`;

    if (pontosValidos.length === 0) {
      statusElement.textContent = pontosAprovados.length === 0
        ? 'Nenhum ponto aprovado para exibir.'
        : 'Nenhum ponto aprovado possui localização válida.';
      mostrarMensagem(statusElement.textContent);
      return;
    }

    statusElement.textContent = pontosInvalidos > 0
      ? `${pontosValidos.length} ponto${pontosValidos.length === 1 ? '' : 's'} exibido${pontosValidos.length === 1 ? '' : 's'}; ${pontosInvalidos} com localização inválida.`
      : 'Pontos aprovados carregados com sucesso.';

    ajustarMapa(pontosValidos);
  } catch (error) {
    console.error(error);
    statusElement.textContent = 'Não foi possível carregar os pontos da API.';
    statusElement.classList.add('error');
    mostrarMensagem('Verifique a comunicação com o backend e tente novamente.');
    totalPointsElement.textContent = '0 pontos';
  }
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
    .addTo(map)
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
    map.setView([Number(pontos[0].latitude), Number(pontos[0].longitude)], 15);
    return;
  }

  const limites = L.latLngBounds(pontos.map((ponto) => [Number(ponto.latitude), Number(ponto.longitude)]));
  map.fitBounds(limites, { padding: [36, 36], maxZoom: 15 });
}

function mostrarMensagem(mensagem) {
  mapMessageElement.textContent = mensagem;
  mapMessageElement.classList.add('visible');
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
