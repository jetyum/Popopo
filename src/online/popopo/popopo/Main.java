package online.popopo.popopo;

import online.popopo.api.MainBase;
import online.popopo.api.io.Property;
import online.popopo.popopo.input.InputListener;
import online.popopo.popopo.console.ConsoleCommand;
import online.popopo.popopo.portal.Portal;
import online.popopo.popopo.portal.PortalCommand;
import online.popopo.popopo.portal.PortalListener;
import online.popopo.popopo.protection.Judge;
import online.popopo.popopo.protection.License;
import online.popopo.popopo.protection.ProtectCommand;
import online.popopo.popopo.protection.Reserve;
import online.popopo.popopo.protection.listener.*;
import online.popopo.popopo.selection.SelectCommand;
import online.popopo.popopo.system.SystemCommand;
import online.popopo.popopo.input.Japanese;
import online.popopo.popopo.domain.TeleportListener;
import online.popopo.popopo.voting.VoteCommand;
import online.popopo.popopo.world.TransferCommand;
import online.popopo.popopo.world.WorldInfo;
import online.popopo.popopo.world.WorldLoader;

import java.util.HashMap;
import java.util.Map;

public class Main extends MainBase {
    @Property(key = "portals")
    private Map<String, Portal> portals = new HashMap<>();
    @Property(key = "reserves")
    private Map<String, Reserve> reserves = new HashMap<>();

    private Japanese jpn = new Japanese();
    private Map<String, WorldInfo> worlds;
    private Map<String, License> licenses;

    @Override
    public void onLoad() {
        super.onLoad();

        loadTheme("theme.yml");
        getIO().read(".data/data.gz", this);
        getIO().readResource("kana.gz", jpn);
        worlds = getIO().readDir("worlds", WorldInfo.class);
        licenses = getIO().readDir("licenses", License.class);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        new WorldLoader().load(this, worlds);

        Judge judge = new Judge(reserves, licenses);

        registerListener(new InputListener(this, jpn));
        registerListener(new TeleportListener(this));
        registerListener(new PortalListener(portals));
        registerListener(new BlockListener(judge));
        registerListener(new EntityListener(judge));
        registerListener(new ExplosionListener(judge));
        registerListener(new PlayerListener(judge));

        registerCommand(new SelectCommand(this));
        registerCommand(new SystemCommand());
        registerCommand(new ConsoleCommand(this));
        registerCommand(new TransferCommand());
        registerCommand(new PortalCommand(this, portals));
        registerCommand(new ProtectCommand(this, judge));
        registerCommand(new VoteCommand(this));
    }

    @Override
    public void onDisable() {
        getIO().write(".data/data.gz", this);
    }
}
