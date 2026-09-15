# PROJETO: MAPA DA ÁGUA

## 1. Contexto acadêmico

Projeto desenvolvido para a Atividade Extensionista II: Tecnologia Aplicada à Inclusão Digital – Projeto, do curso CST em Análise e Desenvolvimento de Sistemas da UNINTER.

Título da proposta original:
"Soluções Tecnológicas para a Fome e a Falta de Água Potável em Áreas Vulneráveis"

Setor de aplicação definido no documento:
- Estado de São Paulo
- Bairro Jardim Pantanal, extremo leste de São Paulo
- Comunidades carentes

ODS selecionados:
- ODS 2 – Fome Zero e Agricultura Sustentável
- ODS 6 – Água Potável e Saneamento
- ODS 11 – Cidades e Comunidades Sustentáveis

Objetivos registrados na proposta:
- Fazer levantamento do que é necessário para combater a fome e fornecer água potável.
- Analisar o que foi levantado e propor soluções plausíveis.
- Desenvolver um aplicativo ou site que auxilie na distribuição de alimentos para pessoas de extrema pobreza e no fornecimento de água potável.

IMPORTANTE:
O projeto que estamos planejando vai priorizar o problema da ÁGUA POTÁVEL. A parte de alimentos pode permanecer como contexto da proposta acadêmica, mas o sistema terá como foco principal o acesso, localização e distribuição de água potável.

A atividade também exige documentação dos resultados, link do GitHub com o código e vídeo de até 5 minutos demonstrando a aplicação na comunidade local.

---

## 2. Ideia do sistema

Nome de trabalho:
MAPA DA ÁGUA

Descrição:
Sistema web especializado em localizar, cadastrar e gerenciar pontos de acesso à água potável e solicitações de abastecimento em áreas vulneráveis.

O sistema NÃO deve tentar recriar o Google Maps inteiro.

A ideia é utilizar uma tecnologia/biblioteca de mapas existente e criar uma aplicação especializada em água potável.

Objetivo principal:
Permitir que uma pessoa encontre pontos próximos onde exista:
- água gratuita;
- água para compra;
- distribuição de água;
- locais que estejam necessitando de água.

---

## 3. Usuários

### Morador
Pode:
- visualizar o mapa;
- pesquisar pontos;
- aplicar filtros;
- visualizar detalhes de pontos;
- encontrar pontos próximos;
- registrar uma necessidade de água;
- acompanhar uma solicitação.

### Distribuidor
Pode:
- cadastrar ponto de distribuição;
- informar disponibilidade;
- atualizar informações do ponto;
- aceitar/gerenciar solicitações;
- marcar solicitação como atendida.

### Administrador
Pode:
- gerenciar usuários;
- aprovar pontos;
- editar/remover pontos;
- visualizar solicitações;
- visualizar estatísticas;
- controlar informações incorretas.

---

## 4. Funcionalidades principais

### Mapa
A tela principal deve mostrar um mapa da região com marcadores.

Legenda planejada:
- 🟢 Água gratuita
- 🔵 Água para compra
- 🟡 Ponto de distribuição
- 🔴 Local necessitando de água

Ao clicar em um marcador, mostrar:
- nome;
- tipo;
- endereço;
- horário;
- disponibilidade;
- observações;
- opção para ver detalhes.

### Cadastro de ponto de água
Campos planejados:
- nome;
- descrição;
- tipo;
- endereço;
- latitude;
- longitude;
- horário inicial;
- horário final;
- disponibilidade;
- observações;
- usuário responsável;
- data de cadastro.

Tipos:
- GRATUITA
- COMPRA
- DISTRIBUICAO

Disponibilidade:
- ALTA
- MEDIA
- BAIXA
- INDISPONIVEL

### Solicitação de água
Campos planejados:
- localização;
- quantidade necessária em litros;
- número de pessoas;
- urgência;
- descrição/observação;
- usuário;
- data;
- status.

Urgência:
- BAIXA
- MEDIA
- ALTA

Fluxo:
CRIADA -> EM_ANALISE -> ACEITA -> EM_DISTRIBUICAO -> ATENDIDA

Também pode existir:
EM_ANALISE -> RECUSADA

### Pontos próximos
O sistema deverá futuramente permitir usar a localização do usuário para encontrar pontos próximos e mostrar distância aproximada.

Exemplo:
- Ponto A — 350 m — disponível
- Ponto B — 720 m — distribuição
- Ponto C — 1,4 km — compra

### Filtros
Filtrar por:
- gratuita;
- compra;
- distribuição;
- necessidade;
- disponibilidade.

### Painel administrativo
Mostrar, por exemplo:
- pontos cadastrados;
- pontos disponíveis;
- solicitações abertas;
- solicitações atendidas;
- solicitações recentes.

---

## 5. Arquitetura planejada

Arquitetura geral:

USUÁRIO
  ↓
FRONTEND
  ↓ HTTP/REST
SPRING BOOT / JAVA
  ↓
BANCO DE DADOS

O backend será responsável por:
- regras de negócio;
- usuários;
- pontos de água;
- solicitações;
- disponibilidade;
- API REST;
- persistência dos dados.

O frontend será responsável por:
- interface;
- mapa;
- marcadores;
- filtros;
- formulários;
- visualização das informações.

---

## 6. Tecnologias planejadas

Primeira versão:

Backend:
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- API REST

Frontend:
- HTML
- CSS
- JavaScript

Banco:
- PostgreSQL

Versionamento:
- Git
- GitHub

Testes:
- JUnit

Mapa:
- Usar uma biblioteca/serviço de mapas web adequado. A tecnologia específica do mapa ainda deve ser escolhida antes da implementação.

Possíveis tecnologias futuras, somente se houver necessidade:
- Spring Security
- JWT
- React
- Docker

NÃO adicionar tecnologias avançadas sem necessidade. Primeiro fazer o MVP funcionar.

---

## 7. Banco de dados inicial

### USUARIO
Campos:
- id
- nome
- email
- senha
- telefone
- tipo_usuario
- data_cadastro

### PONTO_AGUA
Campos:
- id
- nome
- descricao
- tipo
- endereco
- latitude
- longitude
- horario_inicio
- horario_fim
- disponibilidade
- usuario_id
- data_cadastro

### SOLICITACAO
Campos:
- id
- usuario_id
- quantidade_litros
- numero_pessoas
- urgencia
- latitude
- longitude
- descricao
- status
- data_criacao
- data_atualizacao

### ATUALIZACAO
Campos:
- id
- ponto_agua_id
- usuario_id
- disponibilidade
- observacao
- data_atualizacao

Relacionamentos planejados:
- Um USUARIO pode criar várias SOLICITACOES.
- Um USUARIO pode ser responsável por vários PONTOS_AGUA.
- Um PONTO_AGUA pode possuir várias ATUALIZACOES.
- Uma ATUALIZACAO pertence a um PONTO_AGUA e a um USUARIO.

Antes da implementação, produzir um DER formal com PK, FK e cardinalidades.

---

## 8. API REST planejada

### Usuários
POST   /api/usuarios
GET    /api/usuarios/{id}
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}

### Pontos
GET    /api/pontos
GET    /api/pontos/{id}
POST   /api/pontos
PUT    /api/pontos/{id}
DELETE /api/pontos/{id}

### Solicitações
GET    /api/solicitacoes
GET    /api/solicitacoes/{id}
POST   /api/solicitacoes
PUT    /api/solicitacoes/{id}

### Futuro
GET /api/pontos/proximos?latitude=...&longitude=...

Os endpoints podem ser ajustados durante a implementação, mas a API deve seguir princípios REST e manter nomes consistentes.

---

## 9. Estrutura planejada do backend

src/main/java/com/mapaagua/

- controller/
  - UsuarioController
  - PontoAguaController
  - SolicitacaoController

- service/
  - UsuarioService
  - PontoAguaService
  - SolicitacaoService

- repository/
  - UsuarioRepository
  - PontoAguaRepository
  - SolicitacaoRepository

- entity/
  - Usuario
  - PontoAgua
  - Solicitacao
  - Atualizacao

- dto/
  - DTOs necessários para entrada/saída da API

- config/
  - configurações necessárias

Manter separação entre Controller, Service, Repository e Entity.

---

## 10. MVP obrigatório

Antes de implementar funcionalidades avançadas, garantir que o MVP funcione.

MVP:
1. mapa;
2. banco de dados;
3. API Java/Spring Boot;
4. cadastro de pontos;
5. visualização dos pontos;
6. marcadores no mapa;
7. filtros;
8. detalhes do ponto.

Se o MVP estiver funcionando, o projeto já terá um núcleo demonstrável.

Depois adicionar:
- usuários;
- login;
- solicitações;
- status;
- painel administrativo;
- geolocalização;
- estatísticas.

---

## 11. Ordem de desenvolvimento

NÃO começar programando aleatoriamente.

Seguir esta ordem:

1. Requisitos
2. Casos de uso
3. Regras de negócio
4. Protótipo das telas
5. DER
6. Arquitetura
7. Classes Java
8. Endpoints
9. Banco de dados
10. Projeto Spring Boot
11. API
12. Frontend
13. Mapa
14. Integração frontend + API
15. Testes
16. Documentação
17. GitHub
18. Vídeo da atividade

---

## 12. Metodologia acadêmica

A metodologia planejada para o trabalho:

1. Identificação do problema
2. Levantamento das necessidades
3. Definição dos requisitos
4. Modelagem do sistema
5. Modelagem do banco de dados
6. Desenvolvimento da API
7. Desenvolvimento do mapa
8. Integração dos componentes
9. Testes
10. Aplicação na comunidade
11. Avaliação dos resultados
12. Documentação

---

## 13. Cronograma

Prazo de entrega informado: 20/09/2026.

Cronograma planejado:

10–16/08:
- planejamento;
- requisitos;
- casos de uso;
- DER;
- arquitetura;
- telas.

17–23/08:
- Spring Boot;
- banco;
- entidades;
- repositories;
- services;
- controllers;
- API de pontos.

24–30/08:
- frontend;
- mapa;
- marcadores;
- filtros;
- detalhes;
- cadastro de pontos.

31/08–06/09:
- usuários;
- login;
- solicitações;
- status;
- painel administrativo.

07–13/09:
- testes;
- correções;
- documentação;
- GitHub.

14–17/09:
- vídeo;
- apresentação;
- revisão.

18–19/09:
- testes finais;
- correções;
- revisão da entrega.

20/09:
- entrega.

---

## 14. Regras importantes para o desenvolvimento

1. Não criar um clone do Google Maps.
2. O mapa deve servir ao problema de acesso à água.
3. Priorizar água potável.
4. Manter o projeto simples o suficiente para ser concluído no prazo.
5. Não adicionar tecnologias desnecessárias.
6. Não implementar funcionalidades sem antes verificar se fazem sentido para o objetivo.
7. Manter código organizado.
8. Usar Git desde o início.
9. Criar commits claros.
10. Documentar decisões importantes.
11. Não inventar dados reais sobre pontos de água. Dados usados na demonstração devem ser claramente identificados como dados de teste/simulados quando não forem coletados na comunidade.
12. Dados reais da comunidade só devem ser inseridos após levantamento/verificação apropriados.
13. Evitar armazenar dados pessoais desnecessários.
14. O projeto deve poder ser explicado em um vídeo de até 5 minutos.

---

## 15. Objetivo do primeiro ciclo

O primeiro objetivo técnico é conseguir:

1. abrir o frontend;
2. visualizar o mapa;
3. buscar os pontos na API Java;
4. receber os pontos do banco PostgreSQL;
5. colocar os pontos como marcadores no mapa;
6. clicar em um marcador;
7. visualizar os detalhes.

Somente depois disso começar funcionalidades secundárias.

---

## 16. Próximo passo

Antes de programar, produzir:
- documento de requisitos;
- casos de uso;
- regras de negócio;
- protótipo das telas;
- DER completo;
- arquitetura do sistema.

Só depois iniciar a implementação do Spring Boot.

Este arquivo é a fonte inicial de contexto do projeto "Mapa da Água".
