package _new.custom.cuilian.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Attribute {

    public HashMap<String, Double> damage = new HashMap<>();
    public HashMap<String, Double> bloodSuck = new HashMap<>();
    public HashMap<String, Double> jump = new HashMap<>();
    public HashMap<String, Double> defense = new HashMap<>();
    public HashMap<String, Double> reboundDamage = new HashMap<>();
    public HashMap<String, Double> experience = new HashMap<>();
    public List<HashMap<String, Double>> list = new ArrayList<>();

    public Attribute(HashMap h1, HashMap h2, HashMap h3, HashMap h4, HashMap h5, HashMap h6, List<HashMap<String, Double>> list) {
        damage = h1;
        bloodSuck = h2;
        jump = h3;
        defense = h4;
        reboundDamage = h5;
        experience = h6;
        this.list = list;
    }
}
