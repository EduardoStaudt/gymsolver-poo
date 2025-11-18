# GymSolver – Sistema de Gerenciamento de Academia

Projeto final da disciplina **Programação Orientada a Objetos (POO)** –  
Universidade Tecnológica Federal do Paraná – Campus Santa Helena.

## 👥 Equipe

- Brandon Monteiro Donisthorpe – RA: 2758890
- Eduardo Andrei Staudt – RA: 2783045
- Orientador: Prof. Giuvane Conti

---

## 🎯 Objetivo do projeto

O **GymSolver** é um sistema voltado para a administração de academias, com foco em:
- cadastro e gerenciamento de alunos;
- planos de assinatura;
- controle de frequência e pagamentos;
- acompanhamento de treinos;
- geração de relatórios gerenciais e suporte à tomada de decisão.

O projeto está sendo desenvolvido em **Java**, utilizando **POO**, **MVC** e **interfaces gráficas** com Swing ou JavaFX.

---

## 📌 Entregas previstas (etapas do trabalho)

1. **Documento de Análise**
    - Descrição do problema e do sistema.
    - Requisitos funcionais e não funcionais.
    - Casos de uso.
    - Diagrama de classes.
    - Outras seções conforme modelo fornecido pelo professor.

2. **Implementação das Classes (Java)**
    - Implementar as classes definidas no diagrama de classes do documento de análise.
    - Organização em camadas no padrão **MVC**:
        - `model` – regras de negócio e entidades.
        - `controller` – coordena a lógica entre model e view.
        - `view` – telas da aplicação (Swing/JavaFX).

3. **Protótipos de Tela**
    - Implementação das telas principais do sistema utilizando **Swing** ou **JavaFX**.
    - Integração básica com os controllers (sem necessidade de todas as funcionalidades completas nesta fase inicial).

---

## 🧱 Arquitetura e estrutura de pastas

Estrutura sugerida para o repositório:

```text
GymSolver/
├─ docs/
│  ├─ analise/
│  │  ├─ DocumentoAnalise_GymSolver.docx
│  │  └─ diagramas/        # Casos de uso, diagrama de classes, etc.
├─ src/
│  └─ br/utfpr/gymsolver/
│     ├─ model/            # Classes de domínio (Aluno, Plano, Treino, etc.)
│     ├─ view/             # Telas Swing/JavaFX
│     └─ controller/       # Lógica de controle
├─ .gitignore
└─ README.md

