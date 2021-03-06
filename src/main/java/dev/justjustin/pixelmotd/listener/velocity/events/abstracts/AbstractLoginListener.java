package dev.justjustin.pixelmotd.listener.velocity.events.abstracts;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.ConnectionListener;
import dev.justjustin.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.control.Control;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class AbstractLoginListener  extends ConnectionListener<ProxyServer, LoginEvent, Component> {

    public AbstractLoginListener(PixelMOTD<ProxyServer> plugin) {
        super(plugin);
    }

    @Override
    @Subscribe
    public void execute(LoginEvent event) {
        if (event == null) {
            return;
        }

        final Player connection = event.getPlayer();

        if (connection == null) {
            return;
        }

        if (connection.getUniqueId() == null) {

            getLogs().info("The plugin will not apply the whitelist or blacklist check for a null-ip and will block player from joining to the server");



            event.setResult(
                    result("&cBlocking you from joining to the server, reason: &6Null IP&c, if this is a error, contact to the server staff")
            );
            return;

        }

        final InetSocketAddress socketAddress = connection.getRemoteAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            getPlayerDatabase().add(
                    address.getHostAddress(),
                    connection.getUsername()
            );
        }

        final String username = connection.getUsername();

        final String uuid = connection.getUniqueId().toString();

        Control settings = getControl();

        String path = ".global.players.by-";

        if (hasWhitelist()) {
            if (settings.getStringList("whitelist" + path + "name").contains(username) ||
                    !settings.getStringList("whitelist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-whitelist"));

                event.setResult(
                        result(
                                replace(
                                        reason,
                                        "whitelist.global",
                                        username,
                                        uuid
                                )
                        )
                );
                return;
            }
        }

        if (hasBlacklist()) {
            if (settings.getStringList("blacklist" + path + "name").contains(username) ||
                    !settings.getStringList("blacklist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-blacklist"));

                event.setResult(
                        result(
                                replace(
                                        reason,
                                        "blacklist.global",
                                        username,
                                        uuid
                                )
                        )
                );
            }
        }



    }

    private ResultedEvent.ComponentResult result(String message) {
        return ResultedEvent.ComponentResult.denied(
                colorize(message)
        );
    }

    @Override
    public Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}