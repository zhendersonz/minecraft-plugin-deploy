---
name: minecraft-plugin-deploy
description: Ciclo completo de plugins Paper/Spigot — clonar, programar, compilar com Maven, deploy com backup, rollback, reload, e criar novos plugins
license: MIT
metadata:
  stack: java
  tooling: maven
  game: minecraft
  api: paper
---

## Regra de ouro

- **Criar / Editar / Build** → faço na hora, sem perguntar
- **Deploy / Reload / Rollback** → só quando você pedir explicitamente

Segurança automática roda silenciosa nos bastidores.

## Proteção automática (silenciosa)

- ✅ Backup de JAR antes de deploy
- ✅ Backup de .java antes de editar (`<arquivo>.original.bak`)
- ✅ Verificar servidor online antes de reload
- ✅ Build automático após editar código
- ✅ Backup do JAR atual antes de rollback

Se o build falhar: tento corrigir (máx 3x). Se não conseguir, aviso.

Se o servidor crashar pós-deploy: rollback automático + aviso.

## NUNCA faço

- ❌ Deploy sem você pedir "faz deploy" ou "/plugin-deploy"
- ❌ Mexer em paper.jar, server.properties
- ❌ Force push
- ❌ Deletar arquivos sem backup

## O que faz

- **Ler** — explora o código
- **Clonar** — baixa repo do GitHub
- **Programar** — escreve, corrige, refatora
- **Build** — compila com `mvn clean package`
- **Deploy** — só quando você mandar
- **Rollback** — só quando você mandar
- **Template** — cria plugin novo do zero

## Fluxo

### Editar/Criar (automático — sem perguntar)

```
Você: "Cria um plugin de warp chamado WarpPlugin"
  → pergunta parâmetros (só 1x)
  → gera template
  → build
  → "Plugin criado! Build ok. Quer fazer deploy?"

Você: "Adiciona comando /fly no HProtect"
  → lê o código
  → edita
  → build
  → "Comando adicionado! Build passou. Quer deploy?"
```

### Deploy (só quando pedir)

```
Você: "Faz deploy do HProtect"
  → backup automático do JAR antigo
  → copia novo JAR
  → verifica servidor online
  → reload
  → "Deploy feito! Plugin rodando."

Você: "/plugin-deploy"
  → mesma coisa
```

### Rollback (só quando pedir)

```
Você: "Faz rollback do CreativeLogger"
  → lista backups com data e tamanho
  → pergunta qual restaurar
  → você escolhe
  → restaura + reload
```

## Regras de código

### Estrutura
```
com.<plugin>          — Main class
com.<plugin>.commands — CommandExecutors
com.<plugin>.listeners — Listeners
com.<plugin>.managers — Lógica
com.<plugin>.models   — Dados
com.<plugin>.utils    — Utilitários
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

### Evitar
- `Bukkit.getScheduler().scheduleSyncDelayedTask`
- `ItemStack()` sem Material
- `ChatColor.RED` — use `"§c"`
- `getConfig().save(new File(...))` — use `saveConfig()`
- Loops infinitos, null não verificado

## Template

`/plugin-create <nome> <desc>`:
- Pergunta parâmetros (1x)
- Cria e build na hora
- Não faz deploy

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

## Referência

`references/paper-api.md` — padrões de implementação.

## Config

Salvo em `AGENTS.md` (pergunto 1x e guardo):
- `PLUGINS_DIR`
- `PAPER_API_VERSION`
- `JAVA_VERSION`
- `AUTHOR` — ZHendersonZ

## Comandos

| Comando | Ação | Deploy? |
|---------|------|---------|
| `/plugin-fetch <repo>` | Clona/atualiza | Não |
| `/plugin-list` | Lista plugins | Não |
| `/plugin-build` | Compila | Não |
| `/plugin-create <nome>` | Cria plugin novo | Não |
| `/plugin-deploy` | Deploy com backup | **Sim** |
| `/plugin-cycle <repo>` | fetch → editar → build → deploy | **Sim** |
| `/plugin-reload <plugin>` | Dá reload | **Sim** |
| `/plugin-rollback <plugin>` | Restaura backup | **Sim** |
