import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Bot {
    public Bot(){

    }

    public static  List<List<Tile1>> findValidGroups(List<Tile1> tiles, List<Tile1> bots_tiles) {
        List<Tile1> duplicateTiles = tiles.stream()
                .collect(Collectors.groupingBy(t -> new AbstractMap.SimpleEntry<>(t.getColor(), t.getNumber()), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> new Tile1(entry.getKey().getKey(), entry.getKey().getValue()))
                .collect(Collectors.toList());


        // Create a set to store all groups
        Set<Set<Tile1>> allGroups = new HashSet<>();
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);
        colors.add(Color.YELLOW);

        for (int i = 1; i < 14; i++) {
            Set<Tile1> numberGroup = new HashSet<>();

            for (Tile1 otherTile1 : tiles) {
                if (otherTile1.getNumber() == i) {
                    numberGroup.add(otherTile1);
                }
            }
            allGroups.add(numberGroup);
        }

        for (Color c:colors) {
            Set<Tile1> colorGroup = new HashSet<>();
            for (Tile1 otherTile : tiles) {
                if (otherTile.getColor().equals(c)) {
                    colorGroup.add(otherTile);
                }
            }
            allGroups.add(colorGroup);
        }

        // Remove redundant groups
        List<List<Tile1>> validGroups = new ArrayList<>();
        List<List<Tile1>> validNumbers = new ArrayList<>();
        List<List<Tile1>> validColors = new ArrayList<>();

        for (Set<Tile1> group : allGroups) {
            //System.out.println("group: " + group);
            isValidGroupByColor(group, validColors);
            isValidGroupByNumber(group, validNumbers);
        }


        List<List<Tile1>> combinedGroups = new ArrayList<>();
        combinedGroups.addAll(validColors);
        combinedGroups.addAll(validNumbers);


// Sort combinedGroups by length in descending order
        combinedGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));


//        System.out.println("combined before");
//        for (List<Tile1> c:combinedGroups) {
//            System.out.println(c);
//        }

        List<Tile1> troubleTiles = new ArrayList<>();


        boolean foundValidGroup = true;
        while (foundValidGroup) {
            foundValidGroup = handleOneSeriesAtATime(combinedGroups, duplicateTiles,troubleTiles);
        }

//        System.out.println("combined:");
//        for (List<Tile1> c:combinedGroups) {
//            System.out.println(c);
//        }



        int n = 1;
        for (Tile1 t:troubleTiles) {
            //System.out.println(n);
            int maxAllowed = isInDups(duplicateTiles, t) ? 2 : 1;
            int currentCount = countTileOccurrences(t, combinedGroups);
            //System.out.println(t +", max = " + maxAllowed + ", current = "+ currentCount);
            while (currentCount < maxAllowed) {
                boolean cur_changed = false;
                for (List<Tile1> g : combinedGroups) {
                    List<Tile1> temp = new ArrayList<>(g);
                    temp.add(t);
                    temp.sort(Comparator.comparingInt(Tile1::getNumber));
                    if (isValidGroup(temp)) {
                        g.add(t);
                        g.sort(Comparator.comparingInt(Tile1::getNumber));
                        currentCount++;
                        cur_changed = true;
                    }
                    if (cur_changed)
                        break;
                }
                if (!cur_changed)
                    break;
            }
        }

        List<Tile1> leftOvers = new ArrayList<>(tiles);

        for (List<Tile1> group : combinedGroups) {
            for (Tile1 tile : group) {
                Iterator<Tile1> iterator = leftOvers.iterator();
                while (iterator.hasNext()) {
                    Tile1 currentTile = iterator.next();
                    if (currentTile.getNumber() == tile.getNumber() && currentTile.getColor() == tile.getColor()) {
                        iterator.remove();
                        break; // Remove only the first occurrence
                    }
                }
            }
        }

        leftOvers = last_check(leftOvers, combinedGroups);

//        System.out.println("combined:");
//        for (List<Tile1> c:combinedGroups) {
//            System.out.println(c);
//        }

        List<List<Tile1>> check = new ArrayList<>(combinedGroups);
        List<Tile1> checkL = new ArrayList<>(leftOvers);
        for (Tile1 l:checkL) {
            for (List<Tile1> g:check) {
                if(addAndCheck(l,g, combinedGroups)){
                    leftOvers.remove(l);
                    break;
                }
            }
        }
        bots_tiles.clear();
        bots_tiles.addAll(leftOvers);

        return combinedGroups;
    }
    public static boolean addAndCheck(Tile1 t, List<Tile1> list, List<List<Tile1>> combinedGroups){
        int index = 0;
        boolean flag = true;
        for (Tile1 t1 : list) {
            if (t1.getNumber() == t.getNumber() && t1.getColor() == t.getColor()){
                flag = false;
                break;
            }
            else
                index++;
        }

        if (!flag) {

            if (index > 0 && index < list.size() - 1) {
                List<Tile1> firstHalf = new ArrayList<>(list.subList(0, index + 1));
                List<Tile1> secondHalf = new ArrayList<>(list.subList(index + 1, list.size()));

                secondHalf.add(0, t);

                if (firstHalf.size() >= 3 && secondHalf.size() >= 3) {
                    combinedGroups.remove(list);
                    combinedGroups.add(firstHalf);
                    combinedGroups.add(secondHalf);
                    return true;
                }
            }
        }
        else {
            List<Tile1> temp = new ArrayList<>(list);
            temp.add(t);
            temp.sort(Comparator.comparingInt(Tile1::getNumber));
            if(isValidGroup(temp)){
                combinedGroups.remove(list);
                combinedGroups.add(temp);
                return true;
            }
        }
        return false;
    }
    public static boolean isValidGroupByNumber(Set<Tile1> group, List<List<Tile1>> validNumbers) {
        if (group.size() < 3) {
            return false;
        }

        // Check if all tiles have the same number
        int firstNumber = group.iterator().next().getNumber();
        boolean sameNumber = group.stream().allMatch(t -> t.getNumber() == firstNumber);
        if (!sameNumber) {
            return false;
        }

        List<Tile1> tileList = new ArrayList<>(group);
        tileList.sort(Comparator.comparing((Tile1 t) -> {
            Color color = t.getColor();
            if (color.equals(Color.RED)) return 1;
            if (color.equals(Color.BLACK)) return 2;
            if (color.equals(Color.BLUE)) return 3;
            if (color.equals(Color.YELLOW)) return 4;
            return 5; // If there are other colors not specified
        }).thenComparingInt(Tile1::getNumber));


        List<Tile1> duplicateTiles = tileList.stream()
                .collect(Collectors.groupingBy(Tile1::getColor, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .flatMap(entry -> tileList.stream().filter(tile -> tile.getColor().equals(entry.getKey())).limit(1))
                .collect(Collectors.toList());


        //System.out.println(duplicateTiles);


        List<List<Tile1>> possible_groups = new ArrayList<>();

        boolean foundValidGroup = true;
        while (!tileList.isEmpty()) {
            foundValidGroup = isValidPartNumber(tileList, possible_groups);
            if (!foundValidGroup && !tileList.isEmpty()) {
                tileList.remove(0); // Remove the first member if no valid group was found
            }
        }


        //System.out.println("possible_groups numbers: " + possible_groups);

        if(group.size() == 6 && duplicateTiles.size() == 2){
            for (Tile1 t:possible_groups.get(0)) {
                int count = 0;
                for (Tile1 dup:duplicateTiles) {
                    if(t.getColor() != dup.getColor()){
                        count++;
                    }
                }
                if (count == 2){
                    possible_groups.get(0).remove(t);
                    duplicateTiles.add(t);
                    break;
                }
            }
            validNumbers.add(duplicateTiles);
            //System.out.println("dups Numbers: " + duplicateTiles);
        }
        validNumbers.addAll(possible_groups);
        //System.out.println("possible_groups Numbers: " + possible_groups);

        return !possible_groups.isEmpty();
    }
    public static boolean isValidGroupByColor(Set<Tile1> group, List<List<Tile1>> validColors) {
        if (group.size() < 3) {
            return false;
        }
        // Check if all tiles have the same color
        boolean sameColor = group.stream().allMatch(t -> t.getColor().equals(group.iterator().next().getColor()));
        if (!sameColor) {
            return false;
        }

        List<Tile1> tileList = new ArrayList<>(group);
        tileList.sort(Comparator.comparingInt(Tile1::getNumber));


        List<Tile1> duplicateTiles = tileList.stream()
                .collect(Collectors.groupingBy(Tile1::getNumber, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .flatMap(entry -> tileList.stream().filter(tile -> tile.getNumber() == entry.getKey()).limit(1))
                .collect(Collectors.toList());


        List<List<Tile1>> possible_groups = new ArrayList<>();

        boolean foundValidGroup = true;
        while (!tileList.isEmpty()) {
            foundValidGroup = isValidPart(tileList, possible_groups);
            if (!foundValidGroup && !tileList.isEmpty()) {
                tileList.remove(0); // Remove the first member if no valid group was found
            }
        }
        //System.out.println("possible_groups colors: " + possible_groups);

        for (Tile1 duplicate : duplicateTiles) {
            int count = 0;
            List<List<Tile1>> listsWithDuplicate = new ArrayList<>();

            for (List<Tile1> group1 : possible_groups) {
                boolean flag = false;
                for (Tile1 t:group1) {
                    if (t.getColor() == duplicate.getColor() && t.getNumber() == duplicate.getNumber()){
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    listsWithDuplicate.add(group1);
                    count++;
                }
            }



            if (count == 1 && listsWithDuplicate.size() == 1) {
                List<Tile1> group1 = listsWithDuplicate.get(0);
                int index = group1.indexOf(duplicate);

                if (index > 0 && index < group1.size() - 1) {
                    List<Tile1> firstHalf = new ArrayList<>(group1.subList(0, index + 1));
                    List<Tile1> secondHalf = new ArrayList<>(group1.subList(index + 1, group1.size()));

                    secondHalf.add(0, duplicate);

                    if (firstHalf.size() >= 3 && secondHalf.size() >= 3) {
                        possible_groups.remove(group1);
                        possible_groups.add(firstHalf);
                        possible_groups.add(secondHalf);
                    }
                }
            }
        }

//        Set<List<Tile1>> uniqueGroups = new HashSet<>(possible_groups);
//        possible_groups = new ArrayList<>(uniqueGroups);
//
        validColors.addAll(possible_groups);

        return possible_groups.size()>0;
    }
    public static boolean isValidPart(List<Tile1> tileList, List<List<Tile1>> possible_groups) {
        if (tileList.isEmpty()) {
            return false;
        }

        List<Tile1> sideList = new ArrayList<>();
        sideList.add(tileList.get(0));
        int lastNumber = tileList.get(0).getNumber();

        for (int i = 1; i < tileList.size(); i++) {
            if (tileList.get(i).getNumber() == lastNumber + 1) {
                sideList.add(tileList.get(i));
                lastNumber = tileList.get(i).getNumber();

//                if (sideList.size() >= 3) {
//                    possible_groups.add(new ArrayList<>(sideList));
//                }
            }
        }

        if (sideList.size() >= 3) {
            for (Tile1 tile : sideList) {
                tileList.remove(tile);
            }
            possible_groups.add(new ArrayList<>(sideList));
            return true;
        }
        return false;
    }

    public static boolean isValidPartNumber(List<Tile1> tileList, List<List<Tile1>> possible_groups) {
        if (tileList.isEmpty()) {
            return false;
        }

        List<Tile1> sideList = new ArrayList<>();
        sideList.add(tileList.get(0));
        Color lastColor = tileList.get(0).getColor();

        for (int i = 1; i < tileList.size(); i++) {
            if (tileList.get(i).getColor() != lastColor) {
                sideList.add(tileList.get(i));
                lastColor = tileList.get(i).getColor();
//                if (sideList.size() >= 3) {
//                    possible_groups.add(new ArrayList<>(sideList));
//                }
            }
        }

        if (sideList.size() >= 3) {
            for (Tile1 tile : sideList) {
                tileList.remove(tile);
            }
            possible_groups.add(new ArrayList<>(sideList));
            return true;
        }
        return false;
    }

    public static boolean isValidGroup(List<Tile1> group) {
        if (group.size() < 3) return false;

        // Check if it's a valid color group (same color, consecutive numbers)
        boolean sameColor = group.stream().allMatch(t -> t.getColor().equals(group.get(0).getColor()));
        if (sameColor) {
            List<Integer> numbers = group.stream().map(Tile1::getNumber).sorted().collect(Collectors.toList());
            for (int i = 1; i < numbers.size(); i++) {
                if (numbers.get(i) != numbers.get(i - 1) + 1) {
                    return false;
                }
            }
            return true;
        }

        // Check if it's a valid number group (same number, different colors)
        boolean sameNumber = group.stream().allMatch(t -> t.getNumber() == group.get(0).getNumber());
        if (sameNumber) {
            Set<Color> colors = group.stream().map(Tile1::getColor).collect(Collectors.toSet());
            return colors.size() == group.size();
        }

        return false;
    }

    public static int countTileOccurrences(Tile1 tile, List<List<Tile1>> combinedGroups) {
        int count = 0;
        for (List<Tile1> group : combinedGroups) {
            for (Tile1 t : group) {
                if (t.getColor() == tile.getColor() && t.getNumber() == tile.getNumber()) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean isInDups(List<Tile1> duplicateTiles, Tile1 tile1){
        for (Tile1 dups:duplicateTiles) {
            if(tile1.getNumber() == dups.getNumber() && tile1.getColor() == dups.getColor())
                return true;
        }
        return false;
    }

    public static boolean removeTileAndCheckValidGroup(List<Tile1> tiles, Tile1 tile, List<List<Tile1>> changed, List<List<Tile1>> d_changed) {
        // Create a copy of the list to avoid modifying the original list
        List<Tile1> modifiedList = new ArrayList<>(tiles);

        // Find and remove the tile
        Iterator<Tile1> iterator = modifiedList.iterator();
        while (iterator.hasNext()) {
            Tile1 currentTile = iterator.next();
            if (currentTile.getColor() == tile.getColor() && currentTile.getNumber() == tile.getNumber()) {
                iterator.remove();
                break; // Remove only the first occurrence
            }
        }

        // Check if the modified list is a valid group
        if(isValidGroup(modifiedList)){
            changed.add(modifiedList);
            return true;
        }
        d_changed.add(tiles);
        return false;
    }

    public static boolean handleOneSeriesAtATime(List<List<Tile1>> combinedGroups, List<Tile1> duplicateTiles, List<Tile1> troubleTiles) {
        List<List<Tile1>> separateLists = new ArrayList<>();

        for (List<Tile1> group : combinedGroups) {
            for (Tile1 tile : new HashSet<>(group)) { // Use a set to avoid concurrent modification
                int maxAllowed = isInDups(duplicateTiles, tile) ? 2 : 1;
                int currentCount = countTileOccurrences(tile, combinedGroups);

                if (currentCount > maxAllowed) {

                    troubleTiles.add(tile);
                    // Find all series containing this tile and move to separateLists
                    List<List<Tile1>> seriesToRemove = new ArrayList<>();
                    for (List<Tile1> g : combinedGroups) {
                        if (isInDups(g,tile)) {
                            seriesToRemove.add(g);
                        }
                    }
                    separateLists.addAll(seriesToRemove);
                    combinedGroups.removeAll(seriesToRemove);

                    List<List<Tile1>> changed = new ArrayList<>();
                    List<List<Tile1>> d_changed = new ArrayList<>();

                    for (List<Tile1> g:separateLists) {
                        removeTileAndCheckValidGroup(g,tile, changed, d_changed);
                    }
                    separateLists.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));
                    if (d_changed.size() == maxAllowed){
                        combinedGroups.addAll(d_changed);
                        combinedGroups.addAll(changed);
                    } else if (d_changed.size() > maxAllowed) {
                        d_changed.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));
                        for (int i = 0; i < maxAllowed; i++) {
                            combinedGroups.add(d_changed.get(i));
                        }
                        combinedGroups.addAll(changed);
                    }else {

                        changed.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));
                        for (int i = 0; i < maxAllowed-d_changed.size(); i++) {
                            changed.get(0).add(tile);
                            changed.get(0).sort(Comparator.comparingInt(Tile1::getNumber));
                            combinedGroups.add(changed.get(0));
                            changed.remove(0);
                        }
                        combinedGroups.addAll(d_changed);
                        combinedGroups.addAll(changed);
                    }
                    return true; // A valid group was handled
                }
            }
        }

        return false; // No more valid groups to handle
    }

    public static List<Tile1> last_check (List<Tile1> tiles, List<List<Tile1>> combinedGroups1){
        List<Tile1> duplicateTiles = tiles.stream()
                .collect(Collectors.groupingBy(t -> new AbstractMap.SimpleEntry<>(t.getColor(), t.getNumber()), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> new Tile1(entry.getKey().getKey(), entry.getKey().getValue()))
                .collect(Collectors.toList());


        // Create a set to store all groups
        Set<Set<Tile1>> allGroups = new HashSet<>();
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);
        colors.add(Color.YELLOW);

        for (int i = 1; i < 14; i++) {
            Set<Tile1> numberGroup = new HashSet<>();

            for (Tile1 otherTile1 : tiles) {
                if (otherTile1.getNumber() == i) {
                    numberGroup.add(otherTile1);
                }
            }
            allGroups.add(numberGroup);
        }

        for (Color c:colors) {
            Set<Tile1> colorGroup = new HashSet<>();
            for (Tile1 otherTile : tiles) {
                if (otherTile.getColor().equals(c)) {
                    colorGroup.add(otherTile);
                }
            }
            allGroups.add(colorGroup);
        }

        List<List<Tile1>> validNumbers = new ArrayList<>();
        List<List<Tile1>> validColors = new ArrayList<>();

        for (Set<Tile1> group : allGroups) {
            //System.out.println("Lgroup: " + group);
            isValidGroupByColor(group, validColors);
            isValidGroupByNumber(group, validNumbers);
        }


        List<List<Tile1>> combinedGroups = new ArrayList<>();
        combinedGroups.addAll(validColors);
        combinedGroups.addAll(validNumbers);


// Sort combinedGroups by length in descending order
        combinedGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));


//        System.out.println("Lcombined before");
//        for (List<Tile1> c:combinedGroups) {
//            System.out.println(c);
//        }

        List<Tile1> troubleTiles = new ArrayList<>();


        boolean foundValidGroup = true;
        while (foundValidGroup) {
            foundValidGroup = handleOneSeriesAtATime(combinedGroups, duplicateTiles,troubleTiles);
        }

//        System.out.println("Lcombined:");
//        for (List<Tile1> c:combinedGroups) {
//            System.out.println(c);
//        }
//
//        System.out.println("LtroubleTiles: "+troubleTiles.size());


        int n = 1;
        for (Tile1 t:troubleTiles) {
            //System.out.println(n);
            int maxAllowed = isInDups(duplicateTiles, t) ? 2 : 1;
            int currentCount = countTileOccurrences(t, combinedGroups);
            //System.out.println(t +", max = " + maxAllowed + ", current = "+ currentCount);
            while (currentCount < maxAllowed) {
                boolean cur_changed = false;
                for (List<Tile1> g : combinedGroups) {
                    List<Tile1> temp = new ArrayList<>(g);
                    temp.add(t);
                    temp.sort(Comparator.comparingInt(Tile1::getNumber));
                    if (isValidGroup(temp)) {
                        g.add(t);
                        g.sort(Comparator.comparingInt(Tile1::getNumber));
                        currentCount++;
                        cur_changed = true;
                    }
                    if (cur_changed)
                        break;
                }
                if (!cur_changed)
                    break;
            }
        }

        List<Tile1> leftOvers = new ArrayList<>(tiles);

        for (List<Tile1> group : combinedGroups) {
            for (Tile1 tile : group) {
                Iterator<Tile1> iterator = leftOvers.iterator();
                while (iterator.hasNext()) {
                    Tile1 currentTile = iterator.next();
                    if (currentTile.getNumber() == tile.getNumber() && currentTile.getColor().equals(tile.getColor())) {
                        iterator.remove();
                        break; // Remove only the first occurrence
                    }
                }
            }
        }

        combinedGroups1.addAll(combinedGroups);
        return leftOvers;

    }
}
