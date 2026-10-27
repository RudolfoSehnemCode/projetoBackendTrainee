# API Gerenciadora de Projetos

API REST para gerenciar projetos e equipes, desenvolvida como projeto de backend do processo de trainees da Konvex.

**Autor:** Rudolfo Sehnem da Silva

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Spring Web e Validation)
- Maven (com Maven Wrapper incluso)

Os dados são armazenados em memória, então são perdidos quando a aplicação é reiniciada.

## Como rodar

Pré-requisito: Java 21 instalado.

```bash
git clone https://github.com/RudolfoSehnemCode/projetoBackendTrainee
cd backendKonvex/gerenciador-projetos
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

A API sobe em `http://localhost:8080`.

## Entidades

### Equipe

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| id | número | — | Gerado automaticamente |
| lider | texto | Sim | Não pode ser vazio |
| membros | lista de texto | Não | Se não for enviado, vira lista vazia |

### Projeto

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| id | número | — | Gerado automaticamente |
| nome | texto | Sim | Não pode ser vazio |
| cliente | texto | Sim | Não pode ser vazio |
| dataInicio | número | Sim | Timestamp em milissegundos (inteiro) |
| dataEntrega | número | Sim | Timestamp em milissegundos (inteiro) |
| status | texto | — | Sempre `"nao iniciado"` na criação |
| idEquipe | número | Não | Precisa ser o id de uma equipe existente |

## Endpoints

### Equipes

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/equipes` | Cria uma equipe | 201 |
| GET | `/equipes` | Lista todas as equipes | 200 |
| GET | `/equipes/{id}` | Busca uma equipe por id | 200 |
| PUT | `/equipes/{id}` | Edita uma equipe | 200 |
| DELETE | `/equipes/{id}` | Remove uma equipe | 204 |

### Projetos

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/projetos` | Cria um projeto | 201 |
| GET | `/projetos` | Lista todos os projetos | 200 |
| GET | `/projetos/{id}` | Busca um projeto por id | 200 |
| PUT | `/projetos/{id}` | Edita um projeto | 200 |
| DELETE | `/projetos/{id}` | Remove um projeto | 204 |

## Códigos de status

| Código | Significado | Quando acontece |
|---|---|---|
| **200 OK** | Deu certo | Listar, buscar ou editar |
| **201 Created** | Criado | POST com dados válidos |
| **204 No Content** | Removido | DELETE de algo que existe (resposta sem corpo) |
| **400 Bad Request** | Dado inválido | Campo obrigatório ausente ou vazio, data não numérica ou decimal, status inválido, ou `idEquipe` de equipe que não existe |
| **404 Not Found** | Não encontrado | Id inexistente em busca, edição ou remoção |

## Exemplos

Os exemplos seguem a ordem de uso: primeiro se cria a equipe 1, depois o projeto 1 vinculado a ela.

### 201 Created

**Criar uma equipe:**

```bash
curl -X POST http://localhost:8080/equipes \
  -H "Content-Type: application/json" \
  -d '{"lider": "Ana", "membros": ["Bruno", "Carla"]}'
```

Resposta (201):

```json
{
  "id": 1,
  "lider": "Ana",
  "membros": ["Bruno", "Carla"]
}
```

**Criar um projeto vinculado à equipe 1:**

O `status` enviado é ignorado. Todo projeto começa como `"nao iniciado"`.

```bash
curl -X POST http://localhost:8080/projetos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Site institucional", "cliente": "Empresa X", "dataInicio": 1767225600000, "dataEntrega": 1772323200000, "status": "concluido", "idEquipe": 1}'
```

Resposta (201):

```json
{
  "id": 1,
  "nome": "Site institucional",
  "cliente": "Empresa X",
  "dataInicio": 1767225600000,
  "dataEntrega": 1772323200000,
  "status": "nao iniciado",
  "idEquipe": 1
}
```

### 200 OK

**Listar projetos:**

```bash
curl http://localhost:8080/projetos
```

Resposta (200):

```json
[
  {
    "id": 1,
    "nome": "Site institucional",
    "cliente": "Empresa X",
    "dataInicio": 1767225600000,
    "dataEntrega": 1772323200000,
    "status": "nao iniciado",
    "idEquipe": 1
  }
]
```

**Buscar uma equipe por id:**

```bash
curl http://localhost:8080/equipes/1
```

Resposta (200):

```json
{
  "id": 1,
  "lider": "Ana",
  "membros": ["Bruno", "Carla"]
}
```

**Editar só o status do projeto (edição parcial):**

```bash
curl -X PUT http://localhost:8080/projetos/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "em andamento"}'
```

Resposta (200). Só o status muda, os outros campos continuam iguais:

```json
{
  "id": 1,
  "nome": "Site institucional",
  "cliente": "Empresa X",
  "dataInicio": 1767225600000,
  "dataEntrega": 1772323200000,
  "status": "em andamento",
  "idEquipe": 1
}
```

**Editar só o líder da equipe:**

```bash
curl -X PUT http://localhost:8080/equipes/1 \
  -H "Content-Type: application/json" \
  -d '{"lider": "Carla"}'
```

Resposta (200):

```json
{
  "id": 1,
  "lider": "Carla",
  "membros": ["Bruno", "Carla"]
}
```

### 204 No Content

**Remover uma equipe:**

```bash
curl -X DELETE http://localhost:8080/equipes/1
```

Resposta (204), sem corpo. O projeto 1, que era da equipe 1, continua existindo, mas passa a ter `"idEquipe": null`.

**Remover um projeto:**

```bash
curl -X DELETE http://localhost:8080/projetos/1
```

Resposta (204), sem corpo.

### 400 Bad Request

**Criar equipe com líder vazio:**

```bash
curl -X POST http://localhost:8080/equipes \
  -H "Content-Type: application/json" \
  -d '{"lider": ""}'
```

Resposta (400). A equipe não é criada.

**Criar projeto sem os campos obrigatórios:**

```bash
curl -X POST http://localhost:8080/projetos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Site institucional"}'
```

Resposta (400). Faltam `cliente`, `dataInicio` e `dataEntrega`, então o projeto não é criado.

**Criar projeto com data decimal:**

```bash
curl -X POST http://localhost:8080/projetos \
  -H "Content-Type: application/json" \
  -d '{"nome": "App", "cliente": "Empresa Y", "dataInicio": 1767225600000.5, "dataEntrega": 1772323200000}'
```

Resposta (400).

**Editar projeto com status inválido:**

```bash
curl -X PUT http://localhost:8080/projetos/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "pausado"}'
```

Resposta (400). Os status aceitos são `"nao iniciado"`, `"em andamento"` e `"concluido"`.

**Vincular projeto a uma equipe que não existe:**

```bash
curl -X PUT http://localhost:8080/projetos/1 \
  -H "Content-Type: application/json" \
  -d '{"idEquipe": 99}'
```

Resposta (400).

### 404 Not Found

**Buscar uma equipe que não existe:**

```bash
curl http://localhost:8080/equipes/99
```

Resposta (404).

**Editar um projeto que não existe:**

```bash
curl -X PUT http://localhost:8080/projetos/99 \
  -H "Content-Type: application/json" \
  -d '{"nome": "Novo nome"}'
```

Resposta (404).

**Remover um projeto que não existe:**

```bash
curl -X DELETE http://localhost:8080/projetos/99
```

Resposta (404).

## Regras e decisões de implementação

- **Edição parcial:** no PUT, só os campos enviados são alterados; os outros continuam como estavam. O `id` enviado no corpo é ignorado.
- **Criação de projeto:** `id` e `status` enviados no corpo são ignorados; todo projeto começa com status `"nao iniciado"`.
- **Datas:** `dataInicio` e `dataEntrega` são timestamps inteiros em milissegundos. Valores decimais ou não numéricos são rejeitados com 400. O enunciado descrevia `dataEntrega` como booleano, o que foi tratado como erro de digitação. Não há validação de que a entrega seja posterior ao início.
- **Vínculo com equipe:** criar ou editar um projeto com `idEquipe` de uma equipe inexistente retorna 400.
- **Exclusão de equipe:** ao remover uma equipe, os projetos vinculados a ela não são apagados; eles ficam com `idEquipe: null`.
- **Membros:** uma equipe criada sem membros recebe uma lista vazia.
- **Ordem da listagem:** os dados ficam em um `LinkedHashMap`, então as listas voltam na ordem de criação.

## Estrutura

```
gerenciador-projetos/src/main/java/br/com/konvex/gerenciador_projetos/
├── controller/   # Endpoints REST (EquipeController, ProjetoController)
├── model/        # Entidades (Equipe, Projeto)
└── service/      # Regras de negócio e armazenamento em memória
```
