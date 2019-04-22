package myapplication.example.com.myapplication.object;

public class Craftw {
    private String id,name,star,limit,set,type,tp,power,skill_1,skill_2,skill_3,up_1,up_2,up_3;
    private int img;
    public Craftw(String id,String name,String star,String type,String tp,String limit,String set,String power,String skill_1,String skill_2,String skill_3,String up_1,String up_2,String up_3,int img) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.limit = limit;
        this.set = set;
        this.type = type;
        this.tp = tp;
        this.power = power;
        this.skill_1 = skill_1;
        this.skill_2 = skill_2;
        this.skill_3 = skill_3;
        this.up_1 = up_1;
        this.up_2 = up_2;
        this.up_3 = up_3;
        this.img = img;
    }
    public void set_id(String id){ this.id = id; }
    public String get_id(){ return id; }

    public void set_name(String name){ this.name = name; }
    public String get_name(){ return name; }

    public void set_star(String star){ this.star = star; }
    public String get_star(){ return star; }

    public void set_limit(String limit){ this.limit = limit; }
    public String get_limit(){ return limit; }

    public void set_set(String set){ this.set = set; }
    public String get_set(){ return set; }

    public void set_type(String type){ this.type = type; }
    public String get_type(){ return type; }

    public void set_tp(String tp){ this.tp = tp; }
    public String get_tp(){ return tp; }

    public void set_power(String power){ this.power = power; }
    public String get_power(){ return power; }

    public void set_skill_1(String skill_1){ this.skill_1 = skill_1; }
    public String get_skill_1(){ return skill_1; }

    public void set_skill_2(String skill_2){ this.skill_2 = skill_2; }
    public String get_skill_2(){ return skill_2; }

    public void set_skill_3(String skill_3){ this.skill_3 = skill_3; }
    public String get_skill_3(){ return skill_3; }

    public void set_up_1(String up_1){ this.up_1 = up_1; }
    public String get_up_1(){ return up_1; }

    public void set_up_2(String up_2){ this.up_2 = up_2; }
    public String get_up_2(){ return up_2; }

    public void set_up_3(String up_3){ this.up_3 = up_3; }
    public String get_up_3(){ return up_3; }

    public void set_img(int img){ this.img = img; }
    public int get_img(){ return img; }
}

