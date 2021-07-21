# Avaliação Técnica - Fullstack Java #

Avaliação técnica para a posição de Desenvolvedor Fullstack com foco em Java + React.js

### Desafio ###
Desenvolver uma aplicação web que gerencie alocação de profissionais em projetos. O objetivo do sistema deve ser controlar as horas trabalhadas do profissional em cada projeto, ou seja, cada profissional deverá ter a possibilidade de registrar quantas horas e em qual projeto trabalhou.

O sistema deverá ter as seguintes interfaces:

| Interface        | Descrição             |
| ---------------- |---------------------- |
| Autenticação              | Interface para autenticação dos usuários através de e-mail e senha
| Visualização dos projetos      | Interface com listagem para visualização dos projetos com totalizador de horas apontadas em cada projeto 
| Apontamento de horas      | Interface para um profissional realizar o apontamento de horas em um projeto específico informando a data e quantidade de horas trabalhadas.


#### Dados da aplicação ####
Não é intuito da avaliação criar telas para cadastro de usuários e projetos, por isso o sistema deverá já conter os seguintes dados abaixo populados nas tabelas:

| Usuários        |
| ---------------- |
| Administrador
| Programador 1
| Programador 2

| Projetos |
| --- |
| Projeto Cliente A
| Projeto Cliente B
 

#### Regras do negócio: ####
* O usuário **Administrador** deverá ter a possibilidade de visualizar todos os projetos com o total de horas apontadas de todos profissionais naquele projeto
* O **Programador 1** poderá apontar horas somente no **Projeto Cliente A**
* O **Programador 2** poderá apontar horas no **Projeto Cliente A** e também no **Projeto Cliente B**
* Cada profissional poderá visualizar na listagem de projetos somente as suas horas apontadas
 

### Tecnologias obrigatórias ###
* Spring Boot REST (Back-end)
* React.JS + Styled Components + ESLint (As regras estão disponíveis neste mesmo projeto git) + Redux + Saga (Front-end)
* Qualquer banco de dados relacional
* GIT + Gitflow


### Pontos de vantagem ###
* Envio particionado de commits ao repositório
* Documentação via Swagger do Back-end


### Critérios de avaliação ###
* Padrões de código: Clean code, SOLID, outros que julgar interessante utilizar.
* Testes unitários no back-end
* Lógica utilizada para sistema de permissões
* Modelagem de dados

## Publicação ##
Você deverá criar repositórios públicos para o(s) projeto(s) criados e enviar o link dos repositórios para o e-mail do recrutador que está em contato.

### Boa sorte! ###
