package myapplication.example.com.myapplication.object;

public class Card {
    private String id,name,star,attr,race,space,life,atk,reply,sum;
    private int img;
    public Card(String id,String name,String star,String attr,String race,String space,String life,String atk,String reply,String sum,int img) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.attr = attr;
        this.race = race;
        this.space = space;
        this.life = life;
        this.atk = atk;
        this.reply = reply;
        this.sum = sum;
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

    public void set_space(String space){ this.space = space; }
    public String get_space(){ return space; }

    public void set_life(String life){ this.life = life; }
    public String get_life(){ return life; }

    public void set_atk(String atk){ this.atk = atk; }
    public String get_atk(){ return atk; }

    public void set_reply(String reply){ this.reply = reply; }
    public String get_reply(){ return reply; }

    public void set_sum(String sum){ this.sum = sum; }
    public String get_sum(){ return sum; }

    public void set_img(int img){ this.img = img; }
    public int get_img(){ return img; }
}
