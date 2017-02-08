package FileWatcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import musicsubscriber.MainScreen.FXMLMainScreenController;

/**
 *
 * @author Pajama Sammy
 */
public class FileWatcher implements Runnable
{

    WatchService watcher;
    Path directory;
    WatchKey key;
    FXMLMainScreenController application;

    public FileWatcher(Path directory, FXMLMainScreenController application) throws IOException
    {
        this.application = application;
        watcher = FileSystems.getDefault().newWatchService();
        this.directory = directory;
        key = null;

        directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents())
                {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Thread.sleep(500);

                    application.PushUpdate(ev);
                }
                key.reset();
            } catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }
}
