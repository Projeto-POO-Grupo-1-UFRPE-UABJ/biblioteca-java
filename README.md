![Logo](./.github/assets/logo_uabjoteca.png)

![Java versão](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white) ![SQLite](https://img.shields.io/badge/SQLite-%2307405e.svg?logo=sqlite&logoColor=white) ![Gui Swing](https://img.shields.io/badge/GUI-Swing-006B3F?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0id2hpdGUiPjxwYXRoIGQ9Ik0zIDNoMThjMS4xIDAgMiAuOSAyIDJ2MTRjMCAxLjEtLjkgMi0yIDJIM2MtMS4xIDAtMi0uOS0yLTJWNWMwLTEuMS45LTIgMi0yem0wIDRoMThWNUgzdjJ6bTAgMnYxMGgxOFY5SDN6Ii8+PC9zdmc+) ![Git](https://img.shields.io/badge/Git-F05032?logo=git&logoColor=white) ![Disciplina P2](https://img.shields.io/badge/Disciplina-P2-blue) ![UFRPE UABJ](https://img.shields.io/badge/UFRPE-UABJ-1B2D4F) ![Entrega](https://img.shields.io/badge/Entrega-2_de_4-yellow) ![Licença MIT](https://img.shields.io/badge/licença-MIT-green) 

<br>
<br>

> [!NOTE]
> Projeto desenvolvido pelo **Grupo 1** para compor a **2ª VA** de **Programação 2 (2026.2)**.<br>
> Este Trabalho é vinculado ao Bacharelado em Engenharia da Computação da **Universidade Federal Rural de Pernambuco, Unidade Acadêmica de Belo Jardim (UFRPE/UABJ)**.

---

Sistema de biblioteca em Java com telas Swing, banco SQLite e relatório CSV. Controla livros, alunos e professores, empréstimos, devoluções e multas por atraso.

**-Relações:**

Usuário-Aluno e Usuário-Professor (herança): as classes Aluno e Professor estabelecem uma relação de herança direta com a superclasse abstrata Usuario. Desse modo, atributos comuns e essenciais ao domínio (como identificador, nome e matrícula) são centralizados na classe genérica, enquanto as subclasses especializadas implementam os comportamentos polimórficos particulares de sua regra de negócio.


Empréstimo-Usuário e Empréstimo-Livro (agregação): o emprestimo funciona como agregador que referencia as duas classes. Se um empréstimo for excluido, o usuário e o livro não devem ser apagados. O emprestimo é o “todo” e o usuário/livro são as partes. Adotou-se conceitualmente a relação de agregação para evidenciar a independência de ciclo de vida entre as entidades: caso um registro de empréstimo seja encerrado ou excluído do sistema, as instâncias de Usuario e Livro continuam existindo de maneira autônoma no banco de dados, sem sofrer remoção em cascata.


