package ch.heigvd.dto;

import ch.heigvd.models.Application;
import ch.heigvd.models.Badge;

/**
 * @author jfleroy
 */
public class BadgeDTO
{
    private String name;

    private String image;

    private int points;

    private boolean repeatable;


    public Badge buildBadge(Application application){
        return new Badge(name, image, points, repeatable, application);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public boolean isRepeatable()
    {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable)
    {
        this.repeatable = repeatable;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }
}
