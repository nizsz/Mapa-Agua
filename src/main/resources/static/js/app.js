const apiUrl = '/api/pontos';
const pointsContainer = document.getElementById('pontos-container');
const statusElement = document.getElementById('status');
const totalPointsElement = document.getElementById('total-pontos');

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
      pointsContainer.innerHTML = '<div class="empty-state">Nenhum ponto de água cadastrado.</div>';
      totalPointsElement.textContent = '0 pontos';
      statusElement.textContent = 'Nenhum ponto encontrado.';
      return;
    }

    totalPointsElement.textContent = `${pontos.length} ponto${pontos.length === 1 ? '' : 's'}`;
    statusElement.textContent = 'Pontos carregados com sucesso.';

    pointsContainer.innerHTML = pontos.map((ponto) => {
      const tipo = ponto.tipo || 'Não informado';
      const disponibilidade = ponto.disponibilidade || 'Não informado';
      const endereco = ponto.endereco || 'Endereço não informado';
      const descricao = ponto.descricao || 'Sem descrição disponível.';

      return `
        <article class="point-card">
          <h3>${ponto.nome || 'Ponto sem nome'}</h3>
          <div class="point-meta">
            <span class="tag tipo">${tipo}</span>
            <span class="tag disponibilidade">${disponibilidade}</span>
          </div>
          <p><strong>Endereço:</strong> ${endereco}</p>
          <p><strong>Descrição:</strong> ${descricao}</p>
          <p><strong>Horário:</strong> ${formatarHorario(ponto.horarioInicio)} - ${formatarHorario(ponto.horarioFim)}</p>
        </article>
      `;
    }).join('');
  } catch (error) {
    console.error(error);
    statusElement.textContent = 'Não foi possível carregar os pontos da API.';
    pointsContainer.innerHTML = '<div class="empty-state">Não foi possível carregar os pontos. Verifique a API.</div>';
    totalPointsElement.textContent = '0 pontos';
  }
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
