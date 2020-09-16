package com.schneewittchen.rosandroid.ros;

/**
 * ROS topic class for sub/pub nodes.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.09.2020
 */
public class Topic {

    /**
     * Topic name e.g. '/map'
     */
    private String name;

    /**
     * Type of the topic e.g. 'nav_msgs.OccupancyGrid'
     */
    private String type;


    public Topic(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setType(String newtype) {
        this.type = newtype;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;

        } else if (object.getClass() != this.getClass()) {
            return false;
        }

        Topic other = (Topic) object;

        return other.getName().equals(name) && other.getType().equals(type);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + 31 * type.hashCode();
    }
}
