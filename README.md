# API Gerenciadora de Projetos

API REST para gerenciar **projetos** e **equipes**, desenvolvida como projeto de backend do processo de trainees da Konvex.

Autor: Rudolfo Sehnem da Silva

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Spring Web e Validation)
- Maven (com Maven Wrapper incluso)

Os dados são armazenados **em memória**, então são perdidos quando a aplicação é reiniciada.

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

| Campo     | Tipo           | Obrigatório | Observação                          |
|-----------|----------------|-------------|-------------------------------------|
| `id`      | número         | —           | Gerado automaticamente              |
| `lider`   | texto          | Sim         | Não pode ser vazio                  |
| `membros` | lista de texto | Não         | Se não for enviado, vira lista vazia |

### Projeto

| Campo         | Tipo   | Obrigatório | Observação                                           |
|---------------|--------|-------------|------------------------------------------------------|
| `id`          | número | —           | Gerado automaticamente                               |
| `nome`        | texto  | Sim         | Não pode ser vazio                                   |
| `cliente`     | texto  | Sim         | Não pode ser vazio                                   |
| `dataInicio`  | número | Sim         | Timestamp em milissegundos (inteiro)                 |
| `dataEntrega` | número | Sim         | Timestamp em milissegundos (inteiro)                 |
| `status`      | texto  | —           | Sempre `"nao iniciado"` na criação                   |
| `idEquipe`    | número | Não         | Precisa ser o id de uma equipe existente             |

## Endpoints

### Equipes

| Método | Rota             | Descrição               | Sucesso |
|--------|------------------|-------------------------|---------|
| POST   | `/equipes`       | Cria uma equipe         | 201     |
| GET    | `/equipes`       | Lista todas as equipes  | 200     |
| GET    | `/equipes/{id}`  | Busca uma equipe por id | 200     |
| PUT    | `/equipes/{id}`  | Edita uma equipe        | 200     |
| DELETE | `/equipes/{id}`  | Remove uma equipe       | 204     |

### Projetos

| Método | Rota              | Descrição                | Sucesso |
|--------|-------------------|--------------------------|---------|
| POST   | `/projetos`       | Cria um projeto          | 201     |
| GET    | `/projetos`       | Lista todos os projetos  | 200     |
| GET    | `/projetos/{id}`  | Busca um projeto por id  | 200     |
| PUT    | `/projetos/{id}`  | Edita um projeto         | 200     |
| DELETE | `/projetos/{id}`  | Remove um projeto        | 204     |

### Códigos de erro

- **400 Bad Request**: campo obrigatório ausente ou vazio, data não numérica ou decimal, status inválido, ou `idEquipe` de equipe que não existe.
- **404 Not Found**: id inexistente em busca, edição ou remoção.

## Exemplos

Criar uma equipe:

```bash
curl -X POST http://localhost:8080/equipes \
  -H "Content-Type: application/json" \
  -d '{"lider": "Ana", "membros": ["Bruno", "Carla"]}'
```

Criar um projeto vinculado à equipe 1:

```bash
curl -X POST http://localhost:8080/projetos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Site institucional", "cliente": "Empresa X", "dataInicio": 1767225600000, "dataEntrega": 1772323200000, "idEquipe": 1}'
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

Editar só o status do projeto:

```bash
curl -X PUT http://localhost:8080/projetos/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "em andamento"}'
```

Listar projetos:

```bash
curl http://localhost:8080/projetos
```

## Regras e decisões de implementação

- **Edição parcial**: no `PUT`, só os campos enviados são alterados; os outros continuam como estavam. O `id` enviado no corpo é ignorado.
- **Criação de projeto**: `id` e `status` enviados no corpo são ignorados; todo projeto começa com status `"nao iniciado"`.
- **Datas**: `dataInicio` e `dataEntrega` são timestamps inteiros em milissegundos. Valores decimais ou não numéricos são rejeitados com 400. O enunciado descrevia `dataEntrega` como booleano, o que foi tratado como erro de digitação. Não há validação de que a entrega seja posterior ao início.
- **Vínculo com equipe**: criar ou editar um projeto com `idEquipe` de uma equipe inexistente retorna 400.
- **Exclusão de equipe**: ao remover uma equipe, os projetos vinculados a ela não são apagados; eles ficam com `idEquipe: null`.
- **Membros**: uma equipe criada sem `membros` recebe uma lista vazia.

## Estrutura

```
gerenciador-projetos/src/main/java/br/com/konvex/gerenciador_projetos/
├── controller/   # Endpoints REST (EquipeController, ProjetoController)
├── model/        # Entidades (Equipe, Projeto)
└── service/      # Regras de negócio e armazenamento em memória
```
