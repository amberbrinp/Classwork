public class LongestSong implements Command
{
    private Song longestSong;
    private int songLength = 0;
    
    public void execute(Object item)
    {
        CD cd = (CD) item;
        for (int i = 0; i < cd.getNumberOfTracks(); i++)//for (Song s: cd)?
        {
            Song song = cd.getSong(i);
            if (song.getLength() > songLength)
            {
                songLength = song.getLength();
                longestSong = song;
            }
        }
    }
    
    public Song getLongestSong()
    {
        return longestSong;
    }
}