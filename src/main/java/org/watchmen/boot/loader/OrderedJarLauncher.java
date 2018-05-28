package org.watchmen.boot.loader;

import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.archive.Archive;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class OrderedJarLauncher extends JarLauncher {
    private static String[] _args;

    @Override
    protected void postProcessClassPathArchives(List<Archive> archives) throws MalformedURLException {
        log("processing classpath");
        for (int i = 0; i < archives.size(); i++) {
            Archive archive = archives.get(i);
            String[] path = archive.getUrl().getPath().split("/");
            String jarPath = path[path.length - 1];
            int idx = jarPath.lastIndexOf(".jar!");
            if (idx >= 0) {
                String jar = jarPath.substring(0, idx);
                // log("jar={0}", jar);
                for (int j = 0; j < _args.length; j++) {
                    String test = _args[j];
                    if (jar.startsWith(test)) {
                        log("jar={0} starts with arg={1} promoting order", jar, test);
                        archives.remove(archive);
                        archives.add(0, archive);
                        break;
                    }
                }

            }
        }
    }

    public static void main(String[] args) throws Exception {
        log("args={0}", Arrays.toString(args));
        _args = args;
        new OrderedJarLauncher().launch(args);
    }

    static void log(String s, Object... args) {
        System.out.println(MessageFormat.format("OrderedJarLauncher: " + s, args));
    }
}
