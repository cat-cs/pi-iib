# Sistema EcoColeta - Documentação

## 1. Visão Geral

O EcoColeta é um sistema cliente-servidor desenvolvido em Java para gerenciamento de pontos de coleta de resíduos. O sistema permite cadastrar, consultar, editar e excluir pontos de coleta através de uma interface de linha de comando.

## 2. Arquitetura do Sistema

### 2.1 Estrutura do Projeto
```
src/main/java/br/com/pucgo/ecocoleta/
├── Cliente/
│   └── ClienteEcoColeta.java      # Interface do usuário
├── Entidade/
│   └── PontoColeta.java          # Modelo de dados
└── Servidor/
    ├── ServidorEcoColeta.java    # Servidor TCP
    ├── Service.java              # Lógica de negócio
    └── Validador.java           # Validações
```

### 2.2 Padrão Arquitetural
- **Cliente-Servidor**: Comunicação via sockets TCP
- **Multithreading**: Servidor suporta múltiplas conexões simultâneas
- **Separação de Responsabilidades**: Camadas distintas para apresentação, negócio e dados

## 3. Componentes do Sistema

### 3.1 Cliente (ClienteEcoColeta.java)
**Responsabilidades:**
- Interface com o usuário através de menu textual
- Construção de requisições no protocolo definido
- Comunicação com servidor via socket TCP (porta 55555)
- Formatação e exibição de respostas

**Funcionalidades:**
- Menu interativo com 5 opções principais
- Validação básica de entrada do usuário
- Tratamento de erros de conexão

### 3.2 Servidor (ServidorEcoColeta.java)
**Responsabilidades:**
- Aceitar conexões TCP na porta 55555
- Gerenciar múltiplos clientes simultaneamente
- Processar comandos do protocolo
- Coordenar chamadas para camada de serviço

**Características:**
- Servidor concorrente (uma thread por cliente)
- Processamento síncrono de requisições
- Tratamento centralizado de exceções

### 3.3 Serviço (Service.java)
**Responsabilidades:**
- Implementar lógica de negócio
- Gerenciar armazenamento em memória dos pontos
- Processar operações CRUD

**Estruturas de Dados:**
- `ConcurrentHashMap<Integer, PontoColeta>`: Armazenamento thread-safe
- `AtomicInteger`: Geração automática de IDs únicos

### 3.4 Entidade (PontoColeta.java)
**Atributos:**
- `id`: Identificador único (int)
- `endereco`: Localização do ponto (String)
- `tipoResiduo`: Tipos de resíduos aceitos (String)
- `horarioFuncionamento`: Horários de operação (String)

### 3.5 Validador (Validador.java)
**Responsabilidades:**
- Validar formato de comandos recebidos
- Verificar quantidade mínima de parâmetros
- Métodos específicos para cada operação

## 4. Protocolo de Comunicação

### 4.1 Formato das Mensagens
**Estrutura:** `COMANDO;PARAMETRO1;PARAMETRO2;...`

### 4.2 Comandos Suportados

| Comando | Formato | Descrição |
|---------|---------|-----------|
| CADASTRAR | `CADASTRAR;endereco;tipo;horario` | Cadastra novo ponto de coleta |
| LISTAR | `LISTAR` | Lista todos os pontos cadastrados |
| CONSULTAR | `CONSULTAR;tipo_residuo` | Busca pontos por tipo de resíduo |
| EDITAR | `EDITAR;id;endereco;tipo;horario` | Atualiza ponto existente |
| EXCLUIR | `EXCLUIR;id` | Remove ponto de coleta |

### 4.3 Formato das Respostas
**Estrutura:** `STATUS;MENSAGEM`

**Status possíveis:**
- `OK`: Operação realizada com sucesso
- `ERRO`: Falha na operação
- `INFO`: Informação adicional
- Dados diretos (para listagens)

## 5. Fluxo de Funcionamento

### 5.1 Inicialização
1. Servidor inicia na porta 55555
2. Cliente conecta ao servidor (localhost:55555)
3. Cliente exibe menu principal

### 5.2 Processamento de Requisições
1. Usuário seleciona opção no menu
2. Cliente coleta dados necessários
3. Cliente constrói requisição no protocolo
4. Requisição enviada via socket TCP
5. Servidor processa comando
6. Resposta retornada ao cliente
7. Cliente formata e exibe resultado

### 5.3 Operações CRUD

#### Cadastro
- Gera ID automático incremental
- Armazena em ConcurrentHashMap
- Retorna confirmação com ID gerado

#### Listagem
- Itera sobre todos os pontos
- Formata saída com separador "||"
- Retorna lista completa ou mensagem vazia

#### Consulta
- Busca case-insensitive por tipo de resíduo
- Utiliza contains() para busca parcial
- Retorna pontos correspondentes

#### Edição
- Localiza ponto por ID
- Atualiza todos os campos
- Mantém mesmo ID

#### Exclusão
- Remove ponto do HashMap por ID
- Confirma remoção ou informa erro

## 6. Características Técnicas

### 6.1 Tecnologias Utilizadas
- **Java 17**: Linguagem principal
- **Maven**: Gerenciamento de dependências e build
- **Socket TCP**: Comunicação cliente-servidor
- **Multithreading**: Concorrência no servidor

### 6.2 Padrões Implementados
- **Thread-Safety**: ConcurrentHashMap e AtomicInteger
- **Single Responsibility**: Cada classe tem responsabilidade específica
- **Error Handling**: Tratamento centralizado de exceções

### 6.3 Limitações Atuais
- Armazenamento apenas em memória (dados perdidos ao reiniciar)
- Interface apenas textual
- Sem autenticação ou autorização
- Sem persistência de dados

## 7. Como Executar

### 7.1 Compilação
```bash
mvn clean compile
```

### 7.2 Execução do Servidor
```bash
java -cp target/classes br.com.pucgo.ecocoleta.Servidor.ServidorEcoColeta
```

### 7.3 Execução do Cliente
```bash
java -cp target/classes br.com.pucgo.ecocoleta.Cliente.ClienteEcoColeta
```

## 8. Exemplo de Uso

1. **Cadastrar ponto:**
   - Escolher opção 1
   - Informar: "Rua A, 123", "papel,vidro", "8:00-17:00"
   - Sistema retorna: "Ponto de coleta cadastrado com ID: 1"

2. **Listar pontos:**
   - Escolher opção 2
   - Sistema exibe todos os pontos cadastrados

3. **Consultar por tipo:**
   - Escolher opção 3
   - Informar: "papel"
   - Sistema retorna pontos que aceitam papel
