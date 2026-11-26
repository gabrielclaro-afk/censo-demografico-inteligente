# Censo Demográfico Inteligente

Aplicativo Android que mostra os **nomes e sobrenomes mais frequentes do Brasil** segundo o **Censo Demográfico 2022 do IBGE**, por estado ou por município.

## Equipe

- Gabriel Assis Costa de Souza
- Gabriel Silva de Souza Claro
- Luis Felipe de Oliveira Brasil

## O que o app faz

1. Ao abrir, carrega a lista de estados do IBGE.
2. O usuário escolhe um **estado** e vê o ranking de nomes daquele estado: o **1º lugar em destaque** e os seguintes em **barras** proporcionais à frequência.
3. As abas **Mulheres**, **Homens** e **Sobrenomes** alternam entre os três rankings.
4. Com a **busca**, o usuário escolhe um **município** do estado e o ranking passa a ser só daquela cidade.
5. O botão **"Ver estado inteiro"** volta para o ranking do estado.
6. Sem internet, ou se o IBGE responder com erro, aparece um **cartão de erro** com uma mensagem clara e o botão **"Tentar novamente"**.

## APIs do IBGE utilizadas

Base: `https://servicodados.ibge.gov.br/`

| API | Endpoint | Para que serve no app |
|---|---|---|
| Localidades | `GET api/v1/localidades/estados?orderBy=nome` | Lista de estados (id, sigla, nome) |
| Localidades | `GET api/v1/localidades/estados/{uf}/municipios?orderBy=nome` | Municípios do estado escolhido |
| Nomes do Censo 2022 | `GET api/v3/nomes/2022/localidade/{id}` | Rankings de `mulheres`, `homens` e `sobrenomes` da localidade (nome + frequência) |

O `{id}` da API de Nomes é o código IBGE da localidade: **2 dígitos** para estado (ex.: `31` = Minas Gerais) ou **7 dígitos** para município (ex.: `3106200` = Belo Horizonte).

## Arquitetura em camadas

```
app/src/main/java/br/com/censodemografico/
├── models/       Classes que espelham o JSON do IBGE
│   ├── Estado.kt
│   ├── Municipio.kt
│   ├── NomeFrequencia.kt
│   └── NomesLocalidadeResponse.kt
├── services/     Acesso à rede com Retrofit
│   ├── IbgeApi.kt           Interface com os endpoints (@GET)
│   ├── IbgeService.kt       Monta o Retrofit com o conversor Gson
│   ├── IbgeRepository.kt    Ponto único de acesso aos dados para o ViewModel
│   └── MensagemDeErro.kt    Converte falhas de rede/HTTP/JSON em mensagens em português
├── ui/           Interface em Jetpack Compose + ViewModel
│   ├── CensoScreen.kt       Tela (recebe o estado e devolve os eventos do usuário)
│   ├── CensoViewModel.kt    Guarda o estado da tela e chama o repositório
│   ├── CensoUiState.kt      Tudo o que a tela precisa para se desenhar
│   ├── DadosFalsos.kt       Dados de exemplo usados pelos @Preview
│   └── theme/               Cores, tipografia e tema
└── MainActivity.kt   Liga o CensoViewModel à CensoScreen
```

**Fluxo de dados:** o usuário toca na `CensoScreen` → o `CensoViewModel` chama o `IbgeRepository` → o Retrofit (`IbgeApi`) busca o JSON no IBGE e o Gson converte para os `models` → o ViewModel atualiza o `CensoUiState` (um `StateFlow`) → a tela se redesenha sozinha.

Detalhes da implementação:

- Ao escolher um estado, a lista de municípios e o ranking de nomes são buscados **em paralelo**.
- Escolher outra localidade **cancela a busca anterior**, então uma resposta atrasada nunca sobrescreve a mais nova.
- "Tentar novamente" repete exatamente a última operação que falhou.

## Tecnologias

- Kotlin
- Jetpack Compose + Material 3
- ViewModel + StateFlow + Coroutines
- Retrofit + Gson
- Testes unitários com JUnit, kotlinx-coroutines-test e MockWebServer
- Android mínimo: 7.0 (API 24)

## Como executar

1. Clone o repositório:
   ```bash
   git clone <URL-DO-REPOSITÓRIO>
   ```
2. Abra a pasta do projeto no **Android Studio** (versão recente) e espere o **Gradle Sync** terminar.
3. Crie ou escolha um emulador em **Device Manager** (API 24 ou superior).
4. Clique em **Run ▶ 'app'**.

> O app precisa de **internet**, porque todos os dados vêm das APIs do IBGE em tempo real.

Para rodar os testes unitários:

```bash
./gradlew test        # Linux/macOS
gradlew.bat test      # Windows
```

## Prints

| Tela inicial | Resultado do estado | Município | Erro sem internet |
|:---:|:---:|:---:|:---:|
| <img src="docs/prints/tela-inicial.png" width="200"> | <img src="docs/prints/resultado.png" width="200"> | <img src="docs/prints/municipio.png" width="200"> | <img src="docs/prints/erro.png" width="200"> |

## Vídeo de demonstração

[Assista no YouTube](<LINK-DO-VÍDEO>)
