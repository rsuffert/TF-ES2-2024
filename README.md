# Escopo e documentação do trabalho
Este trabalho foi desenvolvido como o trabalho final da disciplina de Engenharia de Software II. Toda documentação, bem como o enunciado do trabalho com a definição do seu escopo, pode ser encontrada na pasta `docs/`.

# Resumo das tarefas do trabalho
## 1. Tarefas de planejamento do trabalho
- [X] Criação dos diagramas ER dos bancos de dados;
- [X] Criação do diagrama de componentes do sistema (microsserviços).

## 2. Tarefas de desenvolvimento do trabalho
* Criação dos arquétipos dos microsserviços no [Spring Initializr](https://start.spring.io/):
    - [ ] `ServicoCadastramento`;
    - [ ] `ServicoAssinaturasValidas`;
    - [ ] `ServicoPagamentos`.
* Implementação do microsserviço `ServicoCadastramento`: cadastramento e manutenção dos dados relativos a clientes, aplicativos e assinaturas (quando uma nova assinatura é cadastrada, o cliente ganha 7 dias grátis).
    - [ ] Entidades de domínio Aplicativo, Cliente e Assinatura, conforme enunciado do trabalho;
    - [ ] Banco de dados de cadastro;
    - [ ] Script de inicialização para popular o banco de dados com ao menos 10 clientes, 5 aplicativos diferentes e 5 assinaturas;
    - [ ] **Endpoint /servcad/clientes [GET]**: lista todos os clientes cadastrados;
    - [ ] **Endpoint /servcad/aplicativos [GET]**: lista todos os aplicativos cadastrados;
    - [ ] **Endpoint /servcad/assinaturas [POST]**: cria uma assinatura;
    - [ ] **Endpoint /servcad/aplicativos/{idAplicativo} [PATCH]**: atualiza o custo mensal do aplicativo;
    - [ ] **Endpoint /servcad/assinaturas/{tipo} [GET]**: retorna todas as assinaturas do tipo indicado (TODAS, ATIVAS ou CANCELADAS);
    - [ ] **Endpoint /servcad/asscli/{codcli} [GET]**: retorna todas as assinaturas do cliente informado;
    - [ ] **Endpoint /servcad/assapp/{codapp} [GET]**: retorna a lista de assinaturas do aplicativo informado;
    - [ ] **Evento PagamentoServicoCadastramento (observar)**: quando for recebido, deverá atualizar a validade da assinatura paga na base de dados.
* Implementação do microsserviço `ServicoAssinaturasValidas`: por questoes de performance, responder rapidamente para os aplicativos se uma determinada assinatura continua ativa ou não.
    - [ ] Entidade de domínio Assinatura, conforme enunciado do trabalho;
    - [ ] Possui cache interna de assinaturas. Se a consulta do aplicativo der hit, retorna a informação armazenada na cache; se der miss, pergunta ao `ServicoCadastramento` e registra a resposta na cache;
    - [ ] Deve ser programado como um microsserviço do qual é possível ter várias instâncias;
    - [ ] **Endpoint /assinvalidas/{codass}**: retorna se a assinatura fornecida permanece válida;
    - [ ] **Evento PagamentoServicoAssinaturaValida (observar)**: remover da cache a entrada correspondente à assinatura paga para manter a consistência (na próxima consulta, solicitará o dado atualizado ao `ServicoCadastramento`.
* Implementação do microsserviço `ServicoPagamentos`: manter o registro dos pagamentos efetuados (este serviço será notificado pelos bancos conveniados cada vez que um pagamento é efetuado).
    - [ ] Entidade de domínio Pagamento, conforme enunciado do trabalho;
    - [ ] Banco de dados de pagamentos efetuados;
    - [ ] **Endpoint /registrarpagamento [POST]**: solicita o registro de um pagamento. Deverá (1) armazenar o pagamento no banco e (2) gerar os eventos assíncronos abaixo para notificar os microsserviços interessados;
    - [ ] **Evento PagamentoServicoCadastramento (gerar)**: notifica o `ServicoCadastramento` que um pagamento foi efetuado;
    - [ ] **Evento PagamentoServicoAssinaturaValida (gerar)**: notifica o `ServicoAssinaturaValida` que um pagamento foi efetuado.
* Implementação do broker de comunicação entre os três microsserviços:
    - [ ] Escolha do broker;
    - [ ] Integração com os microsserviços do sistema.

**OBSERVAÇÃO**: a definição do corpo da requisição e do JSON de resposta para cada endpoint, bem como as informações do corpo dos eventos, estão especificados no enunciado do trabalho.

## 3. Tarefas de implantação do trabalho
- [ ] Criação do diagrama de implantação do sistema;
- [ ] Criação de documento informando o passo a passo para implantar o sistema na nuvem;
- [ ] Gravar vídeo demonstrando a execução do sistema na nuvem.
