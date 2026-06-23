# minecraft-plugin-deploy

Skill para [OpenCode](https://opencode.ai) — automatiza o ciclo completo de desenvolvimento de plugins **Minecraft Paper/Spigot** em Java com Maven.

> ### ⚙️ Para usar no seu servidor
>
> 1. **Fork ou clone** este repositório para `~/.config/opencode/skills/minecraft-plugin-deploy/`
> 2. **Edite `AGENTS.md`** com seus dados:
>    - `GITHUB_USERNAME` — seu usuário do GitHub
>    - `PLUGINS_DIR` — pasta de plugins do seu servidor
>    - `SERVER_DIR` — raiz do seu servidor (onde fica `spigot.yml` / `bukkit.yml`)
> 3. Se não existir, a skill detecta automático na primeira execução
>
> Para testar rapidamente, crie a pasta `C:\Users\HENDI\Desktop\teste\plugins` ou ajuste os caminhos no `AGENTS.md`.

## Funcionalidades

- **Auto-detecção** — detecta Java, Maven, GitHub user, servidor e Paper API automaticamente
- **Git Sync** — fetch + pull antes de editar; commit + push após build (token perguntado na hora, nunca salvo)
- **Clonar** repositórios do GitHub
- **Programar** — editar código, corrigir bugs, adicionar features
- **Compilar** com `mvn clean package` (pom.xml profissional com shade + compilador)
- **Deploy** com backup automático do JAR anterior
- **Rollback** para versões anteriores
- **Template** de novo plugin com estrutura pronta
- **Reload** no servidor
- **GitHub templates** — PR, Issues, Contributing, CI workflows inclusos

## Como instalar

```bash
# Clonar para a pasta de skills do OpenCode
git clone https://github.com/zhendersonz/minecraft-plugin-deploy.git ^
  "%APPDATA%\opencode\skills\minecraft-plugin-deploy"
```

Ou copie manualmente para `~/.config/opencode/skills/minecraft-plugin-deploy/`.

## Comandos

| Comando | Ação |
|---------|------|
| `/plugin-fetch <repo>` | Clona ou atualiza repositório |
| `/plugin-list` | Lista plugins locais |
| `/plugin-build` | Compila o plugin atual |
| `/plugin-create <nome>` | Cria novo plugin do zero |
| `/plugin-deploy` | Deploy com backup automático |
| `/plugin-cycle <repo>` | Fetch → editar → build → deploy |
| `/plugin-reload <plugin>` | Dá reload no plugin |
| `/plugin-rollback <plugin>` | Lista backups e restaura |

## Segurança

- Deploy, reload e rollback **só acontecem quando você pede**
- Backup automático de JARs antes de qualquer deploy
- Backup de arquivos `.java` antes de editar
- Build obrigatório após modificações
- Rollback automático se o servidor crashar pós-deploy
- Git pull só acontece se não houver conflitos locais
- Commit + push **só com sua autorização** (token pedido na hora, nunca salvo)

## Exemplos

```
"Pega o LiveVoice do GitHub"            → já sabe que é zhendersonz/LiveVoice
"Cria um plugin de warp chamado WarpPlugin"
"Adiciona comando /fly no HProtect"      → fetch + pull antes, commit + push depois
"Faz deploy do HProtect"                 → backup automático, só com sua permissão
"Faz rollback do CreativeLogger"
```

## Estrutura

```
minecraft-plugin-deploy/
├── AGENTS.md                       # Config detectada automaticamente
├── SKILL.md                        # Instruções da skill
├── scripts/
│   └── detect-env.cmd              # Script de detecção de ambiente
├── .github/
│   ├── workflows/
│   │   ├── ci.yml                  # CI build + test
│   │   └── release-please.yml      # Release automatizada
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.yml
│   │   └── feature_request.yml
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── CONTRIBUTING.md
├── references/
│   ├── paper-api.md                # Referência técnica Paper API
│   └── testing-guide.md            # Testes com MockBukkit
└── templates/
    └── java-plugin/                # Template de novo plugin
        ├── pom.xml                 # Maven com shade + compilador configurado
        ├── config.yml
        └── src/main/
            ├── java/com/example/
            │   ├── TemplatePlugin.java
            │   ├── commands/BaseCommand.java
            │   └── listeners/BaseListener.java
            └── resources/plugin.yml
```
