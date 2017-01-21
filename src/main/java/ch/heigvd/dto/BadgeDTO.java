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

    public BadgeDTO(String name, String image, int points, boolean repeatable)
    {
        this.name = name;
        this.image = image;
        this.points = points;
        this.repeatable = repeatable;
    }

    public BadgeDTO(Badge badge){
        this.name = badge.getName();
        this.image = badge.getImage();
        this.points = badge.getPoints();
        this.repeatable = badge.isRepeatable();
    }

    static public BadgeDTO fromBadgesList(Badge badge){
        return new BadgeDTO(badge);
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
