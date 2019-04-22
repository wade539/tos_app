package myapplication.example.com.myapplication.object;

public class Craft {
    private String id,name,star,attr,race,type,power,skill_1,skill_2,skill_3;
    private int img;
    public Craft(String id,String name,String star,String attr,String race,String type,String power,String skill_1,String skill_2,String skill_3,int img) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.attr = attr;
        this.race = race;
        this.type = type;
        this.power = power;
        this.skill_1 = skill_1;
        this.skill_2 = skill_2;
        this.skill_3 = skill_3;
        this.img = img;
    }
    public void set_id(String id){ this.id = id; }
    public String get_id(){ return id; }

    public void set_name(String name){ this.name = name; }
    public String get_name(){ return name; }

    public void set_star(String star){ this.star = star; }
    public String get_star(){ return star; }

    public void set_attr(String attr){ this.attr = attr; }
    public String get_attr(){ return attr; }

    public void set_race(String race){ this.race = race; }
    public String get_race(){ return race; }

    public void set_type(String type){ this.type = type; }
    public String get_type(){ return type; }

    public void set_power(String power){ this.power = power; }
    public String get_power(){ return power; }

    public void set_skill_1(String skill_1){ this.skill_1 = skill_1; }
    public String get_skill_1(){ return skill_1; }

    public void set_skill_2(String skill_2){ this.skill_2 = skill_2; }
    public String get_skill_2(){ return skill_2; }

    public void set_skill_3(String skill_3){ this.skill_3 = skill_3; }
    public String get_skill_3(){ return skill_3; }

    public void set_img(int img){ this.img = img; }
    public int get_img(){ return img; }
}
