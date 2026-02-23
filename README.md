# MovieLibraryApp 🎬
![Badge](https://img.shields.io/badge/language-Kotlin-blue?style=for-the-badge)
![Badge](https://img.shields.io/badge/arch-MVVM-orange?style=for-the-badge)
![Badge](https://img.shields.io/badge/minSDK-24-green?style=for-the-badge)

##  Descrição

**MovieLibraryApp** é um catálogo de filmes desenvolvido como parte de um desafio técnico para demonstrar habilidades em desenvolvimento Android moderno. O aplicativo consome a API do [The Movie Database (TMDB)](https://www.themoviedb.org/) para exibir listas de filmes populares, em exibição, e permitir a visualização de detalhes, reviews e filmes similares.

O projeto foi estruturado com foco em boas práticas, escalabilidade e manutenibilidade, utilizando uma arquitetura robusta e tecnologias atuais do ecossistema Android.

## 🛠 Tecnologias e Arquitetura

Este projeto foi construído utilizando um conjunto de ferramentas modernas e padrões de arquitetura para garantir um código limpo, testável e performático.

### Tech Stack
- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **UI:**
  - **Jetpack Compose:** Utilizado para construir a tela de detalhes de forma declarativa e moderna.
  - **XML com ViewBinding:** Utilizado para a construção da tela de listas, demonstrando proficiência em abordagens híbridas.
- **Arquitetura:** **MVVM (Model-View-ViewModel)**, garantindo uma clara separação entre a lógica de UI e as regras de negócio.
- **Concorrência:** **Kotlin Coroutines** com `StateFlow` para um gerenciamento de estado reativo, assíncrono e eficiente.
- **Injeção de Dependência:** **Service Locator (Manual)**, implementado através de um `AppContainer` para gerenciar as dependências do `Repository`.
- **Networking:**
  - [Retrofit](https://square.github.io/retrofit/): Para uma comunicação type-safe com a API REST do TMDB.
  - [Gson](https://github.com/google/gson): Para a desserialização de objetos JSON.
- **Carregamento de Imagens:**
  - [Coil](https://coil-kt.github.io/coil/): Otimizado para Jetpack Compose, utilizado na tela de detalhes.
  - [Glide](https://github.com/bumptech/glide): Utilizado na tela de listas com `RecyclerView`.
- **Gerenciamento de Dependências:** **Gradle Version Catalog (`libs.versions.toml`)**, para uma gestão centralizada e organizada das versões das bibliotecas.

### Destaques Técnicos
- **Repository Pattern:** Abstrai a fonte de dados, separando a lógica de negócio do acesso à API.
- **Tratamento de Estado e Erros:** Uso de uma `sealed class` (`Resource`) para encapsular os estados de UI (Loading, Success, Error), provendo um feedback claro para o usuário em caso de falhas na comunicação com a API.
- **Organização de Pacotes:** Estrutura de pacotes por camada (`data`, `ui`, `util`), promovendo alta coesão e baixo acoplamento.

##  Funcionalidades Implementadas

- **Splash Screen:** Tela de entrada com duração controlada (2 segundos).
- **Tela de Listas:**
  - Exibição de 4 categorias de filmes em `RecyclerViews` horizontais:
    - Em Exibição (`Now Playing`)
    - Em Breve (`Upcoming`)
    - Mais Populares (`Popular`)
    - Melhores Avaliados (`Top Rated`)
- **Tela de Detalhes:**
  - Exibição de pôster, título, sinopse e nota.
  - Conversão da duração de minutos para o formato `X hora(s) Y minuto(s)`.
  - Exibição de Reviews (limitados a 3 linhas).
  - Exibição de Filmes Similares.
- **Tratamento de Erros de API:** A interface exibe um estado de erro caso a chamada à API falhe.
- **Navegação:** Fluxo de navegação completo entre as telas, incluindo o botão "voltar".

## Arquitetura

O projeto adota a arquitetura **MVVM (Model-View-ViewModel)**. A escolha se baseia na excelente separação de responsabilidades que ela proporciona, recomendada oficialmente pelo Google.

- **View (Activity/Composable):** A camada de UI é "burra". Ela apenas observa os estados emitidos pelo ViewModel e reage a eles, sem conter lógica de negócio.
- **ViewModel:** Sobrevive a mudanças de configuração e é responsável por gerenciar o estado da UI, delegando a busca e o processamento de dados para a camada de `Repository`.
- **Model (Repository/API):** Abstrai a origem dos dados. O `MovieRepository` é o único ponto de acesso aos dados para o resto do app, facilitando a manutenção e a possibilidade de adicionar fontes de dados futuras (como um cache local).

##  Como Rodar o Projeto

Para clonar e rodar esta aplicação, siga os passos abaixo.

**Pré-requisitos:**
- Android Studio Iguana | 2023.2.1 ou superior.
- Uma chave de API do The Movie Database (TMDB).

**Instruções:**

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/LeandroBryto/MovieLibraryApp.git
   ```

2. **Modifique o arquivo `Constants.kt`:**
   Para este projeto de desafio, a chave de API está diretamente no código para facilitar a avaliação. Abra o arquivo:
   `app/src/main/java/com/leandro/movielibraryapp/util/Constants.kt`
   
   E certifique-se de que sua chave esteja lá. **Nota:** Em um projeto de produção, a chave deve ser movida para o `local.properties` e acessada via `BuildConfig` para maior segurança.

3. **Sincronize e Rode o projeto no Android Studio.**

##  Diferenciais e Boas Práticas

- **Código Limpo:** Nomenclatura clara e consistente para variáveis, métodos e classes.
- **Organização:** Pacotes bem definidos que facilitam a navegação e a manutenção do projeto.
- **Modernidade:** Uso de `StateFlow` para gerenciamento de estado e `Jetpack Compose` para construção de UI, demonstrando alinhamento com as tendências do ecossistema Android.
