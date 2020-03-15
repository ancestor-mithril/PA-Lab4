import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    Resident[] r;
    List<Resident> residentList;
    Hospital[] h;
    List<Hospital> hospitalList;
    Map<Resident, List<Hospital>> resPrefMap;
    Map<Hospital, List<Resident>> hosPrefMap;
    Map<Hospital, List<List<Resident>>>  hosPrefBigMap;
    Map<Resident, List<List<Hospital>>>  resPrefBigMap;
    Set<Hospital> hospitalSet;
    public static void main(String[] args){
        System.out.println("Hajimari no hajimari");
        Main mainObj=new Main();
        mainObj.initialize();
        System.out.println("Hajimari no owari");
        //mainObj.resolve();
        System.out.println("Owari no hajimari");
        mainObj.randomInstance();
        System.out.println("Owari no owari");
        //mainObj.resolve();


        mainObj.bonus();
        mainObj.resolveBonus();
    }


    /**
     * using Faker name for instances are generated
     * Hospitals have a random capacity from 0 to 20 (they tend to be low)
     * there are 20 Residents
     * with an unknown random probability, a Hospital prefers Residents and a Resident prefers Hospitals
     */
    public void randomInstance(){
        residentList.clear();  hosPrefMap.clear();
        r=null; h=null; resPrefMap.clear(); hospitalSet.clear();
        for (int i=0; i<20; i++){
            residentList.add(new Resident(new Faker().name().fullName()));
        }
        for (int i=0; i<10; i++){
            hospitalSet.add(new Hospital(new Faker().company().name(), (int)Math.ceil(Math.random()*20)));
        }
        for (Resident r : residentList){
            List<Hospital> randomList=new ArrayList<>();
            for (Hospital h:hospitalSet)
                if (Math.random()<Math.random()*3)
                    randomList.add(h);
            resPrefMap.put(r, randomList);
        }
        for (Hospital h : hospitalSet){
            List<Resident> randomList=new ArrayList<>();
            for (Resident r : residentList)
                if (Math.random()<Math.random()*3)
                    randomList.add(r);
            hosPrefMap.put(h, randomList);
        }
        //System.out.println(hosPrefMap);
        //new Faker().company().name();
    }

    /**
     * compulsory copy-pasted (and understood) from slides
     */
    public void initialize(){
        r= IntStream.rangeClosed(0,3)
                .mapToObj(i->new Resident("R"+i))
                .toArray(Resident[]::new);
        residentList=new ArrayList<Resident>(Arrays.asList(r));
        Collections.sort(residentList,
                (Comparator.comparing(Resident::getName)));
        /*
        List<Resident> newSortedList = residentList.stream()
                .sorted(Comparator.comparing(Resident::getName))
                .collect(Collectors.toList());
         */
        h=IntStream.rangeClosed(0,2)
                .mapToObj(i->new Hospital("H"+i))
                .toArray(Hospital[]::new);
        h[0].setCapacity(1);
        h[1].setCapacity(2);
        h[2].setCapacity(2);
        hospitalSet = new TreeSet<Hospital>(Arrays.asList(h));
        /*    hospitalList=new ArrayList<Hospital>
                (Arrays.asList(h)); */
        resPrefMap = new HashMap<>();
        resPrefMap.put(r[0], Arrays.asList(h[0], h[1], h[2]));
        resPrefMap.put(r[1], Arrays.asList(h[0], h[1], h[2]));
        resPrefMap.put(r[2], Arrays.asList(h[0], h[1]));
        resPrefMap.put(r[3], Arrays.asList(h[0], h[2]));
       // System.out.println(resPrefMap.toString());

        hosPrefMap = new TreeMap<>();
        hosPrefMap.put(h[0], Arrays.asList(r[3], r[0], r[1], r[2]));
        hosPrefMap.put(h[1], Arrays.asList(r[0], r[2], r[1]));
        hosPrefMap.put(h[2], Arrays.asList(r[0], r[1], r[3]));
        System.out.println(hosPrefMap.toString());



        residentList.stream()
                .filter(res -> resPrefMap.get(res).contains(h[0]))
                .forEach(System.out::println);
        residentList.stream()
                .filter(res -> resPrefMap.get(res).contains(h[2]))
                .forEach(System.out::println);



        List<Hospital> target = Arrays.asList(h[0], h[2]);
        List<Resident> result = residentList.stream()
                .filter(res -> resPrefMap.get(res).containsAll(target))
                .collect(Collectors.toList());
        System.out.println(result);

        hospitalSet.stream()
                .filter(res -> hosPrefMap.get(res).indexOf(r[0])==0)
                .forEach(System.out::println);

    }




    /**
     * while this looks like a first come  first served algorithm, it is not
     * it's my version of Deferred Late Acceptance – Gale Shapley
     * As long as there is a Resident in stack, the algorithm iterates through his preferences
     * and should it find lower-ranked patients for a Hospital, the Resident is inserted before him
     * eliminated residents are put back in the stack
     */
    public void resolve(){
        Stack<Resident> stack=new Stack<>();
        for (Resident r:residentList)
            stack.push(r);

        while(!stack.empty()){
            Resident r=stack.pop();

            if (resPrefMap.get(r)!=null)
                for (Hospital h: resPrefMap.get(r))
                    if (hosPrefMap.get(h).contains(r)){
                        if (h.getResidentList().size()<h.getCapacity()){
                            h.addResident(r);
                            break;
                        } else {
                            List<Resident> list=hosPrefMap.get(h);
                            for (Resident r1:h.getResidentList())
                                if (list.indexOf(r)<list.indexOf(r1)||!list.contains(r1)){
                                    h.removeResident(r1);
                                    h.addResident(r);
                                    stack.push(r1);
                                    break;
                                }
                        }
                    }
        }
        for (Resident r :residentList)
            System.out.println(r+" "+r.getAssignedHospital());
    }

    /**
     * initializer for bonus
     *
     */
    public void bonus(){
        r= IntStream.rangeClosed(0,3)
                .mapToObj(i->new Resident("R"+i))
                .toArray(Resident[]::new);
        residentList=new ArrayList<Resident>(Arrays.asList(r));
        hospitalSet = new TreeSet<Hospital>(Arrays.asList(IntStream.rangeClosed(0,2)
                .mapToObj(i->new Hospital("H"+i, (i+1)/2+1))
                .toArray(Hospital[]::new)));
        h=IntStream.rangeClosed(0,2)
                .mapToObj(i->new Hospital("H"+i))
                .toArray(Hospital[]::new);
        h[0].setCapacity(1);
        h[1].setCapacity(2);
        h[2].setCapacity(2);
        resPrefBigMap=new HashMap<>();
        resPrefBigMap.put(r[0], Arrays.asList(Arrays.asList(h[0]), Arrays.asList(h[1], h[2])));
        resPrefBigMap.put(r[1], Arrays.asList(Arrays.asList(h[0]), Arrays.asList(h[1], h[2])));
        resPrefBigMap.put(r[2], Arrays.asList(Arrays.asList(h[0]), Arrays.asList(h[1])));
        resPrefBigMap.put(r[3], Arrays.asList(Arrays.asList(h[0]), Arrays.asList(h[2])));
        hosPrefBigMap = new TreeMap<>();
        hosPrefBigMap.put(h[0], Arrays.asList(Arrays.asList(r[3], r[0]) , Arrays.asList(r[1], r[2])));
        hosPrefBigMap.put(h[1], Arrays.asList(Arrays.asList(r[0], r[2]), Arrays.asList(r[1])));
        hosPrefBigMap.put(h[2], Arrays.asList(Arrays.asList(r[0]), Arrays.asList(r[1], r[3])));

    }

    /**
     * same as resolve, but now get's bonus preferences and takes it into consideration
     */
    public void resolveBonus() {
        List<Hospital> hospitals=new ArrayList<>();
        List<Resident> residents=new ArrayList<>();
        Stack<Resident> stack=new Stack<>();
        for (Resident r:residentList)
            stack.push(r);
        while(!stack.empty()){
            Resident r=stack.pop();
            List<List<Hospital>> x=resPrefBigMap.get(r);
            hospitals.clear();
            for (List<Hospital> var1: x)
                for (Hospital var2 : var1)
                    hospitals.add(var2);
            if (hospitals!=null)
                for (Hospital h: hospitals){
                    residents.clear();
                    List<List<Resident>> y=hosPrefBigMap.get(h);
                    for (List<Resident> var1: y)
                        for (Resident var2 : var1)
                            residents.add(var2);
                    if(residents.contains(r)){
                        if (h.getResidentList().size()<h.getCapacity()){
                            h.addResident(r);
                            break;
                        } else {
                            label:
                            for (Resident r1:h.getResidentList()){
                                for (List<Resident> var1: y)
                                    if (var1.contains(r1))
                                        break;
                                    else if (var1.contains(r)){
                                        h.removeResident(r1);
                                        h.addResident(r);
                                        stack.push(r1);
                                        break label;
                                    }
                            }
                        }
                    }
                }
        }
         for (Resident r :residentList)
            System.out.println(r+" "+r.getAssignedHospital());
    }

    /**
     * Ultimul punct de bonus la PA
     * Consideram un digraf bipartit, cu bipartitiile S si T
     * S = {spitale}, T={pacienti}
     * Consideram un punct s si un punct t
     * Fie E multimea tutorul muchiilor, ij \in E daca i \in S, j in T si j \in hosPrefMap.get(i)
     * si ji \in E daca i \in S, j in T si i \in resPrefMap.get(j)
     * si muchii si, pt \forall i \in S
     * si muchii jt, \forall j \in T
     * Consideram un flux pe Digraful de mai sus
     * pe fiecare muchie si si jt avem capacitatea +infinit
     * pe fiecare muchie ij avem capacitatea (numarul de preferinte ale lui i)-(pozitia de preferinta a lui j in i)
     * pt ji ce ii mai sus dar invers
     *
     * => se poate rezolva problema prin aplicarea unui algoritm de flux maxim
     * conform cursurilor de AGhe, atunci cand avem mai mult de o posibilitate de deplasare dintr-un nod in celalalt care au capacitati egale
     * => se obtin 2 sau mai multe fluxuri diferite, insa cu aceeasi valoare
     * => ce spune in cerinta
     */
}
