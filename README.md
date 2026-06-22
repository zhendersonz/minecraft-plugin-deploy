# minecraft-plugin-deploy

Skill para [OpenCode](https://opencode.ai) — automatiza o ciclo completo de desenvolvimento de plugins **Minecraft Paper/Spigot** em Java com Maven.

## Funcionalidades

- **Clonar** repositórios do GitHub
- **Programar** — editar código, corrigir bugs, adicionar features
- **Compilar** com `mvn clean package`
- **Deploy** com backup automático do JAR anterior
- **Rollback** para versões anteriores
- **Template** de novo plugin com estrutura pronta
- **Reload** no servidor via PlugMan ou /reload

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

## Exemplos

```
"Pega o LiveVoice do GitHub"
"Cria um plugin de warp chamado WarpPlugin"
"Adiciona comando /fly no HProtect, build e deploy"
"Faz rollback do CreativeLogger"
```

## Estrutura

```
minecraft-plugin-deploy/
├── SKILL.md                        # Instruções da skill
├── references/
│   └── paper-api.md                # Referência técnica Paper API
└── templates/
    └── java-plugin/                # Template de novo plugin
        ├── pom.xml
        ├── config.yml
        └── src/main/
            ├── java/com/example/
            │   ├── TemplatePlugin.java
            │   ├── commands/BaseCommand.java
            │   └── listeners/BaseListener.java
            └── resources/plugin.yml
```
