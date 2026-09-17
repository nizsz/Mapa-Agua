# 💧 Mapa da Água

Sistema web desenvolvido como projeto extensionista do curso de **Análise e Desenvolvimento de Sistemas**, com o objetivo de apoiar o acesso à água potável em áreas vulneráveis.

O projeto utiliza um mapa interativo para localizar pontos de água e disponibiliza um fluxo de solicitações de abastecimento, permitindo organizar a participação de moradores, distribuidores e administradores.

---

## 📌 Sobre o projeto

O **Mapa da Água** surgiu a partir da problemática do acesso à água potável em comunidades vulneráveis, tendo como contexto o **Jardim Pantanal, no extremo leste de São Paulo**.

A proposta é utilizar a tecnologia como ferramenta de apoio para:

* localizar pontos de acesso à água;
* cadastrar novos pontos de água;
* organizar e aprovar informações cadastradas;
* registrar necessidades de abastecimento;
* acompanhar solicitações;
* organizar o atendimento realizado pelos distribuidores.

O projeto foi desenvolvido inicialmente dentro de uma proposta mais ampla relacionada à fome e à falta de água potável. Durante a implementação, o desenvolvimento foi concentrado na problemática da **água potável**, permitindo entregar uma solução funcional dentro do escopo e do período disponíveis.

---

## 🎯 Objetivos

### Objetivo geral

Desenvolver uma aplicação web capaz de apoiar a localização e o gerenciamento de pontos de acesso à água potável, além de organizar solicitações de abastecimento.

### Objetivos específicos

* disponibilizar um mapa interativo de pontos de água;
* permitir o cadastro e gerenciamento de pontos;
* permitir a aprovação ou rejeição de pontos cadastrados;
* implementar diferentes perfis de usuário;
* permitir que moradores registrem solicitações de abastecimento;
* permitir que administradores analisem as solicitações;
* permitir que distribuidores assumam e atendam solicitações;
* armazenar as informações de forma estruturada em banco de dados;
* aplicar autenticação e controle de acesso conforme o perfil do usuário.

---

## 👥 Perfis de usuário

O sistema possui três perfis principais.

### 🏠 Morador

O morador pode:

* visualizar os pontos de água;
* cadastrar pontos de água;
* solicitar abastecimento;
* acompanhar suas próprias solicitações.

### 🚚 Distribuidor

O distribuidor pode:

* visualizar solicitações disponíveis;
* assumir solicitações;
* iniciar a distribuição;
* marcar solicitações como atendidas;
* cadastrar pontos de água.

### 🛡️ Administrador

O administrador possui funções de gerenciamento e supervisão, podendo:

* visualizar os dados do sistema;
* cadastrar pontos de água;
* aprovar ou rejeitar pontos;
* visualizar solicitações;
* colocar solicitações em análise;
* aceitar ou recusar solicitações.

---

## 🗺️ Principais funcionalidades

### Mapa de pontos de água

O sistema utiliza um mapa interativo para apresentar os pontos cadastrados.

Os pontos podem representar diferentes tipos de acesso à água, como:

* água gratuita;
* água para compra;
* pontos de distribuição.

Também são disponibilizados recursos de busca e filtros para facilitar a localização das informações.

### Cadastro e gerenciamento de pontos

Usuários autorizados podem cadastrar pontos de água informando dados como:

* nome;
* descrição;
* tipo;
* endereço;
* latitude;
* longitude;
* horário de funcionamento;
* disponibilidade.

Os pontos passam por controle de aprovação quando necessário.

### Sistema de solicitações

O sistema possui um fluxo específico para necessidades de abastecimento.

Fluxo principal:

```text
CRIADA
   ↓
EM_ANALISE
   ↓
ACEITA
   ↓
EM_DISTRIBUICAO
   ↓
ATENDIDA
```

Fluxo alternativo:

```text
EM_ANALISE
   ↓
RECUSADA
```

Esse fluxo permite acompanhar a solicitação desde o registro da necessidade até o atendimento.

### Autenticação e autorização

O sistema possui autenticação de usuários e controle de acesso baseado em perfil.

Cada perfil possui permissões específicas para evitar que usuários executem operações que não fazem parte de suas responsabilidades.

---

## 🏗️ Arquitetura

A aplicação utiliza uma arquitetura dividida em frontend, backend e banco de dados:

```text
┌───────────────────────────────┐
│           FRONTEND            │
│       HTML + CSS + JS         │
│           Leaflet             │
└───────────────┬───────────────┘
                │
                │ HTTP / REST
                ▼
┌───────────────────────────────┐
│            BACKEND            │
│       Java + Spring Boot      │
│       Spring Data JPA         │
│        API REST               │
└───────────────┬───────────────┘
                │
                │ JPA / Hibernate
                ▼
┌───────────────────────────────┐
│          PostgreSQL           │
└───────────────────────────────┘
```

---

## 🛠️ Tecnologias utilizadas

### Frontend

* HTML5
* CSS3
* JavaScript
* Leaflet

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* API REST
* Bean Validation
* Spring Security

### Banco de dados

* PostgreSQL

### Ferramentas

* Visual Studio Code
* Maven
* Git
* GitHub

---

## 📂 Estrutura principal do projeto

```text
mapa-da-agua/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mapaagua/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── enums/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   └── js/
│   │       │       └── app.js
│   │       └── application.properties
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## ⚙️ Como executar o projeto

### Pré-requisitos

Antes de executar o projeto, é necessário possuir:

* Java JDK 17 ou superior;
* Maven 3.9 ou superior;
* PostgreSQL;
* Git.

### 1. Clonar o repositório

```bash
git clone SEU_LINK_DO_GITHUB
```

Entre na pasta:

```bash
cd mapa-da-agua
```

### 2. Criar o banco de dados

Crie um banco PostgreSQL chamado:

```text
mapa_agua
```

Configure as credenciais de acesso de acordo com o ambiente local.

> As credenciais do banco não devem ser armazenadas no repositório.

### 3. Configurar as variáveis de ambiente

Configure as variáveis utilizadas pela aplicação:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Exemplo:

```text
DB_URL=jdbc:postgresql://localhost:5432/mapa_agua
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
```

Não publique credenciais reais no GitHub.

### 4. Executar a aplicação

Com Maven:

```bash
mvn spring-boot:run
```

Depois acesse:

```text
http://localhost:8080
```

---

## 🔐 Segurança

O projeto utiliza autenticação e autorização para controlar o acesso às funcionalidades.

Entre os mecanismos utilizados estão:

* autenticação de usuários;
* controle de acesso por perfil;
* proteção das operações de alteração;
* proteção CSRF;
* validação dos dados recebidos pela API;
* controle de propriedade de pontos e solicitações.

Informações sensíveis, como senhas e credenciais do banco de dados, não devem ser armazenadas no código-fonte ou publicadas no repositório.

---

## ✅ Testes realizados

Durante o desenvolvimento foram realizados testes relacionados a:

* autenticação;
* recuperação de sessão;
* permissões por perfil;
* cadastro de pontos;
* atualização de pontos;
* exclusão de pontos;
* aprovação e rejeição de pontos;
* controle de propriedade de pontos;
* criação de solicitações;
* análise de solicitações;
* aceitação e recusa;
* atribuição de distribuidores;
* início da distribuição;
* conclusão do atendimento;
* integração entre frontend e backend.

Também foi realizada validação da sintaxe do JavaScript com:

```bash
node --check src/main/resources/static/js/app.js
```

---

## 📊 Resultados

O resultado do projeto foi um **MVP funcional de uma aplicação web para apoio ao acesso à água potável**.

A aplicação permite centralizar informações sobre pontos de água e organizar solicitações de abastecimento, utilizando diferentes perfis de usuário para representar as responsabilidades existentes no processo.

O projeto também possibilitou a aplicação prática de conhecimentos de:

* desenvolvimento web;
* programação orientada a objetos;
* desenvolvimento de APIs;
* bancos de dados relacionais;
* autenticação e autorização;
* modelagem de sistemas;
* integração entre frontend e backend;
* controle de versão.

---

## 🚧 Limitações

A proposta inicial do projeto também contemplava funcionalidades relacionadas à questão da alimentação e da fome.

Durante o desenvolvimento, entretanto, essa parte não foi implementada integralmente. O escopo foi concentrado na problemática do **acesso à água potável e do abastecimento**, permitindo priorizar uma solução funcional dentro do prazo disponível.

Entre possíveis evoluções futuras estão:

* inclusão de funcionalidades relacionadas à distribuição de alimentos;
* melhoria da geolocalização;
* notificações;
* histórico mais detalhado de solicitações;
* indicadores e relatórios;
* melhorias de acessibilidade;
* expansão do sistema para outras regiões.

---

## 📚 Projeto acadêmico

Projeto desenvolvido como atividade extensionista do curso de:

**Tecnologia em Análise e Desenvolvimento de Sistemas — UNINTER**

Tema relacionado aos Objetivos de Desenvolvimento Sustentável:

* **ODS 2 — Fome Zero e Agricultura Sustentável**
* **ODS 6 — Água Potável e Saneamento**
* **ODS 11 — Cidades e Comunidades Sustentáveis**

---

## 📹 Demonstração

Vídeo demonstrativo do projeto:

**[INSERIR LINK DO VÍDEO]**

O vídeo apresenta o funcionamento da aplicação e os principais fluxos implementados.

---

## 🔗 Links

**Repositório GitHub:**
[Mapa Água](https://github.com/nizsz/Mapa-Agua)

**Vídeo demonstrativo:**
[YOUTUBE](https://youtu.be/tnb_GItX0aM)

---

## 👨‍💻 Desenvolvimento

Projeto desenvolvido para fins acadêmicos e de extensão universitária, com foco na aplicação prática de tecnologias de desenvolvimento de software para uma problemática social.

---
