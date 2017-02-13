package MusicCollection;

import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author Pajama Sammy
 */
public interface MusicCollection
{
    public boolean AddSong(File file);
    public boolean DeleteSong(Path path);
}
