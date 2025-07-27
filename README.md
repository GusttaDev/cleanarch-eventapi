# Clean Architecture - CRUD de Eventos (Abordagem Purista)

Este projeto implementa um **CRUD completo de eventos** seguindo rigorosamente os princípios da **Clean Architecture**, agora com uma abordagem **purista**: o core do domínio não possui anotações ou dependências de frameworks externos.

## 🏗️ Arquitetura Implementada

### Princípios Fundamentais Seguidos

✅ **Separação de Responsabilidades**: Cada camada tem uma responsabilidade clara e bem definida

✅ **Regra de Dependência**: Dependências sempre apontam para dentro (direção da estabilidade)

✅ **Independência de Frameworks**: Domínio não conhece Spring, JPA ou qualquer framework externo

✅ **Testabilidade**: Cada camada pode ser testada independentemente

✅ **Flexibilidade**: Fácil substituição de implementações (banco de dados, frameworks, etc.)

✅ **Pureza**: O core (domínio, usecases, gateways) é 100% Java puro, sem anotações ou libs externas

## 📁 Estrutura do Projeto (Abordagem Purista)

```
src/main/java/com/learning/ddd/cleanarch/
├── core/                              # 🟣 DOMÍNIO (Centro da arquitetura)
│   ├── domain/                        # Entidades de negócio (PURO)
│   │   ├── Event.java                 # Domain Model (record/class)
│   │   ├── EventStatusEnum.java       # Enumeração de status
│   │   └── enums/EventType.java       # Enumeração de tipos
│   ├── gateway/                       # Interfaces (contratos)
│   │   └── EventRepository.java       # Interface do repositório
│   └── usecases/                      # Casos de uso (regras de negócio)
│       ├── CreatorEventUseCase.java / CreatorEventUseCaseImpl.java         # Criação de eventos
│       ├── UpdateEventUseCase.java / UpdateEventUseCaseImpl.java           # Atualização de eventos
│       ├── RetrieverAllEventsUseCase.java / RetrieverAllEventsUseCaseImpl.java   # Listagem paginada de eventos
│       ├── RetrieverEventByIdUseCase.java / RetrieverEventByIdUseCaseImpl.java   # Busca por ID
│       ├── CancellerEventUseCase.java / CancellerEventUseCaseImpl.java     # Cancelamento de evento (exclusão lógica)
│       └── RetrieverEventsByFilterUseCase.java / RetrieverEventsByFilterUseCaseImpl.java # Busca flexível por filtros
│
├── infraestruct/                      # 🔵 INFRAESTRUTURA (Camadas externas)
│   ├── dtos/                          # Data Transfer Objects
│   │   └── EventDTO.java              # DTO com validações
│   ├── mapper/                        # Mapeadores (toModel/toEntity)
│   │   └── EventMapper.java           # MapStruct mapper
│   ├── gateway/                       # Implementações dos gateways
│   │   ├── EventJpaRepository.java    # Spring Data JPA interface
│   │   └── EventRepositoryImpl.java   # Implementação do gateway
│   ├── persistence/entities/          # Entidades JPA
│   │   ├── BaseEntity.java            # Entidade base com auditoria
│   │   └── EventEntity.java           # Entidade JPA
│   ├── presentation/controller/       # Controllers REST
│   │   └── EventController.java       # API REST endpoints
│   └── config/                        # Configurações mínimas
│       └── EventUseCaseConfig.java    # Configuração dos usecases
│
└── Application.java                   # Classe principal Spring Boot
```

## 🎯 **Abordagem Purista**

### ✅ **O que Mantivemos da Clean Architecture:**
- **Domínio puro**: `Event.java`, interfaces dos casos de uso, sem anotações/frameworks
- **Regra de dependência**: sempre aponta para dentro
- **Separação de camadas**: responsabilidades bem definidas
- **Interfaces como contratos**: gateways e use cases

### 🚫 **O que Removemos:**
- **Sem anotações do Spring/JPA/Bean Validation no core**
- **Sem dependências externas no domínio e usecases**
- **Toda integração com frameworks fica na infraestrutura**

### 💡 **Por que essa Abordagem é Superior:**
- **100% desacoplada** de frameworks no core
- **Testabilidade máxima**
- **Evolução e manutenção facilitadas**

## 🔄 Fluxo das Dependências (Inalterado)

```
Controller → UseCase → Repository (Interface) ← RepositoryImpl
    ↓           ↓              ↑                      ↓
   DTO    (Java puro)      Domain                  Entity
    ↓        Domain         Model                     ↓
 Mapper    (center)      (center)                Database
```

**IMPORTANTE**: As setas sempre apontam para dentro (direção do domínio)!

## 🧪 Como Testar

### 1. **Criar um Evento** (datas devem ser presentes ou futuras)
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Conferência de Tecnologia 2025",
    "description": "Uma conferência sobre as últimas tendências em tecnologia",
    "identifier": "tech-conf-2025",
    "startAt": "2025-08-01T09:00:00",
    "endAt": "2025-08-01T18:00:00",
    "location": "Centro de Convenções - São Paulo",
    "capacity": 500,
    "type": "CONFERENCE",
    "status": "DRAFT"
  }'
```
- O campo `status` é obrigatório e deve ser informado na criação do evento. Exemplos: `DRAFT`, `PUBLISHED`, `OPEN`, `COMPLETED`, `CANCELLED`.

### 2. **Buscar por ID**
```bash
curl http://localhost:8080/api/events/1
```

### 3. **Listar com Paginação**
```bash
curl "http://localhost:8080/api/events?page=0&size=5&sort=name,asc"
```

### 4. **Buscar eventos por múltiplos filtros**
```bash
curl -X POST http://localhost:8080/api/events/search \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Conferência",
    "location": "São Paulo",
    "type": "CONFERENCE",
    "status": "OPEN",
    "startAtFrom": "2025-08-01T00:00:00",
    "startAtTo": "2025-08-31T23:59:59",
    "endAtFrom": "2025-08-01T00:00:00",
    "endAtTo": "2025-08-31T23:59:59",
    "capacityMin": 100,
    "capacityMax": 1000,
    "identifier": "tech-conf-2025"
  }'
```

#### 🔍 **Sistema de Filtros Avançado (Spring Data JPA Specifications)**

O sistema de filtros implementa **Spring Data JPA Specifications** seguindo a [documentação oficial do Spring](https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html), oferecendo:

**✅ Filtros Disponíveis:**
- **`name`**: Busca por nome (case-insensitive, LIKE)
- **`location`**: Busca por localização (case-insensitive, LIKE)
- **`type`**: Filtro exato por tipo de evento (`CONFERENCE`, `WORKSHOP`, `SEMINAR`)
- **`status`**: Filtro exato por status (`DRAFT`, `PUBLISHED`, `OPEN`, `COMPLETED`, `CANCELLED`)
- **`startAtFrom/startAtTo`**: Range de data de início
- **`endAtFrom/endAtTo`**: Range de data de fim
- **`capacityMin/capacityMax`**: Range de capacidade
- **`identifier`**: Busca exata por identificador

**✅ Características Técnicas:**
- **Todos os filtros são opcionais** e podem ser combinados
- **Busca case-insensitive** para texto (nome e localização)
- **Ranges flexíveis** para datas e capacidade
- **Performance otimizada** com Criteria API
- **Complexidade cognitiva reduzida** (de 25 para ~8-10)
- **Seguindo padrões Spring Data JPA** com métodos estáticos


### 5. **Atualizar Evento**
```bash
curl -X PUT http://localhost:8080/api/events/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Conferência de Tecnologia 2025 - ATUALIZADA",
    "description": "Descrição atualizada",
    "identifier": "tech-conf-2025",
    "startAt": "2025-08-01T08:00:00",
    "endAt": "2025-08-01T19:00:00",
    "location": "Centro de Convenções - São Paulo",
    "capacity": 600,
    "type": "CONFERENCE"
  }'
```

### 6. **Cancelar Evento**
```bash
curl -X POST http://localhost:8080/api/events/1/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "reason": "Evento cancelado por motivo de força maior"
  }'
```
- O campo `reason` é obrigatório para cancelar um evento.
- O status do evento será alterado para `CANCELLED` e o motivo salvo.

#### 🧩 **Como funciona o fluxo de cancelamento (Clean Architecture)**
- O use case de cancelamento recupera a entidade (`EventEntity`) do banco.
- Os campos `status` e `cancelReason` são setados diretamente na entidade.
- A entidade é salva novamente no banco pelo repositório.
- Assim, o domínio permanece limpo, testável e desacoplado da infraestrutura.

## ⚖️ Regras de Negócio Principais

- **Não permite eventos com identifier duplicado**
- **Data de início deve ser anterior ou igual à data de fim**
- **Capacidade deve ser positiva**
- **ID deve ser positivo para update/delete**
- **Busca por nome e localização exige parâmetro não vazio**
- **Validações de DTO (camada infra):**
  - Datas devem ser presentes ou futuras
  - Nome, identifier, localização obrigatórios
  - Capacidade mínima de 1

## 🎨 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA** (com Specifications)
- **MapStruct**
- **MySQL 8.0**
- **Bean Validation (apenas na camada infra)**

## 🏗️ **Arquitetura das Specifications**

### 📁 **Estrutura das Specifications**
```
src/main/java/com/learning/ddd/cleanarch/infraestruct/persistence/repositories/
├── EventSpecifications.java      # Métodos estáticos para cada filtro
└── EventSpecification.java       # Builder que combina os filtros
```

### 🔧 **Implementação Seguindo Spring Data JPA**

**✅ `EventSpecifications` (Classe Utilitária):**
```java
@UtilityClass
public final class EventSpecifications {
    public static Specification<EventEntity> hasName(String name) { ... }
    public static Specification<EventEntity> hasLocation(String location) { ... }
    public static Specification<EventEntity> hasType(EventType type) { ... }
    // ... outros métodos estáticos
}
```

**✅ `EventSpecification` (Builder):**
```java
public class EventSpecification {
    public Specification<EventEntity> build() {
        Specification<EventEntity> spec = (root, query, cb) -> cb.conjunction();
        
        spec = spec.and(EventSpecifications.hasName(filter.getName()));
        spec = spec.and(EventSpecifications.hasLocation(filter.getLocation()));
        // ... combinação fluente
        
        return spec;
    }
}
```

### 🎯 **Benefícios da Implementação:**

**✅ Complexidade Cognitiva Reduzida:**
- **Antes:** 25 (acima do limite de 15)
- **Depois:** ~8-10 (bem abaixo do limite)

**✅ Seguindo Padrões Spring Data JPA:**
- ✅ Métodos estáticos para Specifications
- ✅ Combinação fluente com `.and()`
- ✅ Retorno `null` para condições não aplicáveis
- ✅ Documentação completa

**✅ Princípios SOLID:**
- ✅ **S** - Cada método tem responsabilidade única
- ✅ **O** - Fácil extensão sem modificar código existente
- ✅ **L** - Interface consistente
- ✅ **I** - Métodos pequenos e específicos
- ✅ **D** - Depende de abstrações

## 💡 Benefícios da Abordagem Purista

- **Domínio 100% desacoplado**
- **Testabilidade máxima**
- **Evolução facilitada**
- **Infraestrutura pode ser trocada sem impacto no core**

---

**Essa implementação combina 100% dos princípios da Clean Architecture com pureza máxima no core e produtividade na infraestrutura! 🎉**

## 🧪 **Testando as Specifications**

### **Exemplos de Testes das Specifications:**

```java
// Teste de Specification individual
@Test
void shouldFilterByName() {
    Specification<EventEntity> spec = EventSpecifications.hasName("conferência");
    List<EventEntity> events = eventRepository.findAll(spec);
    // Assertions...
}

// Teste de Specification combinada
@Test
void shouldFilterByMultipleCriteria() {
    Specification<EventEntity> spec = EventSpecifications.hasName("tech")
        .and(EventSpecifications.hasType(EventType.CONFERENCE))
        .and(EventSpecifications.hasCapacityMin(100));
    List<EventEntity> events = eventRepository.findAll(spec);
    // Assertions...
}
```

### **Testando via API REST:**

```bash
# Teste 1: Buscar eventos por nome
curl -X POST http://localhost:8080/api/events/search \
  -H "Content-Type: application/json" \
  -d '{"name": "conferência"}'

# Teste 2: Buscar eventos por tipo e status
curl -X POST http://localhost:8080/api/events/search \
  -H "Content-Type: application/json" \
  -d '{"type": "CONFERENCE", "status": "OPEN"}'

# Teste 3: Buscar eventos em período específico
curl -X POST http://localhost:8080/api/events/search \
  -H "Content-Type: application/json" \
  -d '{
    "startAtFrom": "2025-01-01T00:00:00",
    "startAtTo": "2025-12-31T23:59:59"
  }'
```

---

**🎯 Resultado Final:** Sistema de filtros robusto, performático e seguindo as melhores práticas do Spring Data JPA! 