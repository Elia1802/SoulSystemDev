package de.elia;

import de.elia.systemclasses.annotations.ToBeDeleted;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import org.jetbrains.annotations.NotNull;

@Deprecated//USE THE LOADER OF THE SYSTEM
@ToBeDeleted(removalVersion = "5.0.0", reason = "A loader exists")
public class Loader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        //...
    }
}
