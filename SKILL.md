---
name: minecraft-plugin-deploy
description: Automatiza o ciclo completo de desenvolvimento de plugins Paper/Spigot — clonar, programar, compilar com Maven, deploy com backup, rollback, reload e criar novos plugins do zero. Ative quando o usuário mencionar plugins Minecraft, Paper, Spigot, Maven, ou comandos /plugin-*
license: MIT
metadata:
  stack: java
  tooling: maven
  game: minecraft
  api: paper
  audience: developers
  category: deployment
---

## Procedimento obrigatório ao ser ativada

### Passo 1: Leia `AGENTS.md` AGORA

Use `Read` em `AGENTS.md`. Se o arquivo existir, extraia:
- `GITHUB_USERNAME` → use para TODAS as chamadas ao GitHub
- `PLUGINS_DIR` → use para deploy, listagem local
- `SERVER_DIR` → use para reload
- `JAVA_VERSION`, `MAVEN_HOME`, `PAPER_API_VERSION` → use para build

**NÃO pergunte nada ao usuário. Use esses valores diretamente.**

### Passo 2: Se `AGENTS.md` não existir

Rode detecção automática:

| Variável | Fonte |
|----------|-------|
| `GITHUB_USERNAME` | `git config user.name` |
| `JAVA_VERSION` | `java -version` |
| `MAVEN_HOME` | `mvn --version` |
| `PLUGINS_DIR` | Procura `plugins/` junto com `spigot.yml`/`bukkit.yml` |
| `SERVER_DIR` | Detectado por `spigot.yml`/`bukkit.yml` |
| `PAPER_API_VERSION` | De `pom.xml` existente ou usa `1.21.4-R0.1-SNAPSHOT` |

Se algo falhar na detecção, **só então** pergunte ao usuário (1 única vez) e salve em `AGENTS.md`.

## Regra de ouro

- **Criar / Editar / Build** → faço na hora, sem perguntar
- **Deploy / Reload / Rollback** → só quando você pedir explicitamente
- **Token GitHub** → nunca salvo, perguntado na hora do push

## Quando usar esta skill

- "Cria um plugin de X" — gerar novo plugin do template
- "Adiciona comando /X no Y" — editar plugin existente
- "Faz deploy do X" — copiar JAR e recarregar servidor
- "Pega o X do GitHub" — clonar repositório
- "/plugin-repos" — listar repositórios do seu GitHub
- "/plugin-list" — listar plugins locais
- "/plugin-rollback X" — restaurar backup anterior

## Proteção automática

- Backup de JAR antes de deploy
- Backup de .java antes de editar (`<arquivo>.original.bak`)
- Verificar servidor online antes de reload
- Build automático após editar código
- Backup do JAR atual antes de rollback
- Git pull só se não houver conflitos locais

Se o build falhar: tento corrigir (máx 3x). Se não conseguir, aviso.

## NUNCA faço

- Deploy sem você pedir "faz deploy" ou "/plugin-deploy"
- Mexer em paper.jar, server.properties
- Force push
- Deletar arquivos sem backup
- Salvar token GitHub em arquivo

## Overview

A skill cobre o pipeline completo:

1. **Ler** — explora código de plugins existentes
2. **Listar** — lista TODOS os repositórios do seu GitHub (paginação completa)
3. **Clonar** — baixa repositório do GitHub
4. **Programar** — escreve, corrige, refatora código
5. **Build** — compila com `mvn clean package`
6. **Git Sync** — fetch + pull antes de editar; commit + push após build (token perguntado na hora, nunca salvo)
7. **Deploy** — só quando você mandar
8. **Rollback** — só quando você mandar
9. **Template** — cria plugin novo do zero com estrutura pronta

## Fluxo de trabalho

### Editar / Criar

```
Você: "Cria um plugin de warp chamado WarpPlugin"
  → pergunta parâmetros (só 1x)
  → gera template
  → build
  → "Plugin criado! Build ok. Quer fazer deploy?"

Você: "Adiciona comando /fly no HProtect"
  → git fetch origin (se repo Git)
  → git pull --rebase (se main/master e sem conflitos)
  → lê o código
  → edita
  → build
  → "Build passou. Quer commitar e dar push?"
  → se sim: pergunta o token GitHub, commit + push
  → "Quer fazer deploy?"
```

### Deploy

```
Você: "Faz deploy do HProtect"
  → backup do JAR antigo
  → copia novo JAR
  → verifica servidor online
  → reload
  → "Deploy feito! Plugin rodando."
```

### Rollback

```
Você: "Faz rollback do CreativeLogger"
  → lista backups com data e tamanho
  → pergunta qual restaurar
  → restaura + reload
```

## Regras de código

### Estrutura de pacotes

```
com.<plugin>             — Main class
com.<plugin>.commands    — CommandExecutors
com.<plugin>.listeners   — Listeners
com.<plugin>.managers    — Lógica
com.<plugin>.models      — Dados
com.<plugin>.utils       — Utilitários
```

### Convenções

- `onEnable` só registra comandos/listeners/config
- Classes separadas pra cada comando/listener
- `instance = this` (singleton)
- `var` pra tipos óbvios
- `switch` expression
- `record` pra dados imutáveis
- try-with-resources
- NUNCA `@EventHandler` na Main
- Valide permissão em comandos admin
- Prefira Adventure API / MiniMessage para mensagens

### Evitar

- `Bukkit.getScheduler().scheduleSyncDelayedTask`
- `ItemStack()` sem Material
- `ChatColor.RED` — use Adventure: `Component.text("texto", NamedTextColor.RED)`
- `getConfig().save(new File(...))` — use `saveConfig()`
- Loops infinitos, null não verificado
- `e.printStackTrace()` — use `getLogger()` ou SLF4J

## Template de novo plugin

`/plugin-create <nome> <desc>`: pergunta parâmetros 1x, cria e build na hora. Não faz deploy.

```
<nome>/
├── pom.xml
├── config.yml
├── src/main/java/com/<grupo>/
│   ├── <Nome>Plugin.java
│   ├── commands/<Nome>Command.java
│   └── listeners/<Nome>Listener.java
└── src/main/resources/plugin.yml
```

## Referências

- `references/paper-api.md` — padrões de implementação Paper API
- `references/testing-guide.md` — testes com MockBukkit
- `templates/java-plugin/` — template de plugin

## Checklist de implementação

```
Progresso:
- [ ] Auto-detecção concluída (AGENTS.md)
- [ ] Código lido e entendido
- [ ] Alterações feitas
- [ ] Build passou (mvn clean package)
- [ ] Git commit + push (opcional, token perguntado)
- [ ] Deploy realizado (opcional, só com permissão)
- [ ] Reload verificado
```

## Comandos

| Comando | Ação | Deploy? |
|---------|------|---------|
| `/plugin-repos` | Lista TODOS os repositórios do GitHub | Não |
| `/plugin-fetch <repo>` | Clona/atualiza | Não |
| `/plugin-list` | Lista plugins locais | Não |
| `/plugin-build` | Compila | Não |
| `/plugin-create <nome>` | Cria plugin novo | Não |
| `/plugin-deploy` | Deploy com backup | **Sim** |
| `/plugin-cycle <repo>` | fetch → editar → build → deploy | **Sim** |
| `/plugin-reload <plugin>` | Dá reload | **Sim** |
| `/plugin-rollback <plugin>` | Restaura backup | **Sim** |
