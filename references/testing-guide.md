# Testes com MockBukkit

## Setup

Adicione ao `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.mockbukkit.mockbukkit</groupId>
        <artifactId>mockbukkit-v1.21</artifactId>
        <version>4.0.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.11.4</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Exemplo de teste

```java
package com.seuplugin;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeuPluginTest {

    private ServerMock server;
    private MeuPlugin plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(MeuPlugin.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void pluginLoads() {
        assertNotNull(plugin);
    }

    @Test
    void commandWorks() {
        PlayerMock player = server.addPlayer();
        boolean result = server.executeCommand(player, "meucomando", new String[]{"help"});
        assertTrue(result);
    }
}
```

## O que testar

- Comandos (validação de args, permissões)
- Handlers de eventos
- Lógica de negócio
- Serialização de config
- Queries de banco de dados (com H2)

## Dica

A classe principal do plugin **não pode ser `final`**. Se uma API Bukkit não for implementada, `UnimplementedOperationException` é lançado.
