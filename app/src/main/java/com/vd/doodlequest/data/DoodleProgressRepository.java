package com.vd.doodlequest.data;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoodleProgressRepository {

    private static List<String> HINTS = Arrays.asList(
            "It soars through clouds and takes you to faraway places.",
            "It rudely interrupts your peaceful dreams every morning.",
            "This vehicle races to help people in medical emergencies.",
            "A heavenly being with wings and a glowing halo.",
            "When creatures travel together in large groups to find warmer weather.",
            "A tiny insect that can carry objects many times its own weight.",
            "A heavy metal block where blacksmiths hammer hot iron into shape.",
            "The forbidden fruit that fell on Newton's head.",
            "The body part that connects your shoulder to your hand.",
            "A green vegetable that looks like tiny spears growing from the ground.",
            "A sharp tool used by lumberjacks to chop down trees.",
            "A bag you wear on your shoulders to carry school supplies or hiking gear.",
            "A yellow fruit that monkeys love to eat and comedians slip on.",
            "A sticky strip that covers cuts and scrapes to help them heal.",
            "A large red building on a farm where animals and hay are stored.",
            "A white ball with red stitches used in America's favorite pastime.",
            "A wooden stick used to hit home runs and grand slams.",
            "A woven container perfect for carrying fruit or laundry.",
            "An orange ball that players try to shoot through a hoop.",
            "A flying mammal that hangs upside down and comes out at night.",
            "A large basin where you soak in warm water and bubbles.",
            "A sandy place where waves meet the shore and people build sandcastles.",
            "A large, furry animal that hibernates in winter and loves honey.",
            "Facial hair that grows on a man's chin and cheeks.",
            "A soft, comfortable place where you sleep and have dreams.",
            "A buzzing insect that makes honey and can give you a painful sting.",
            "A leather strap that goes around your waist to hold up your pants.",
            "A long seat where people sit in parks or wait for the bus.",
            "A two-wheeled vehicle you pedal with your feet to get around.",
            "Two telescopes joined together to see distant objects up close.",
            "A feathered creature that builds nests and sings beautiful songs.",
            "A sweet dessert decorated with candles that you blow out while making a wish.",
            "A dark purple fruit that grows on thorny bushes.",
            "A small, round fruit that's perfect in muffins and pancakes.",
            "Pages bound together filled with stories, knowledge, and adventures.",
            "An Australian throwing stick that magically returns to your hand.",
            "A small metal lid that seals your favorite fizzy drink.",
            "A fancy necktie that looks like a butterfly and makes you look dapper.",
            "Jewelry that wraps around your wrist like a decorative chain.",
            "The wrinkled organ inside your skull that controls all your thoughts.",
            "A baked food made from flour that's perfect for sandwiches and toast.",
            "A structure that helps you cross over rivers, valleys, or roads.",
            "A green vegetable that looks like tiny trees on your dinner plate.",
            "A long-handled tool with bristles used to sweep away dust and dirt.",
            "A round container with a handle, perfect for carrying water or sand.",
            "A powerful yellow machine that pushes dirt and demolishes buildings.",
            "A large vehicle that carries many passengers along city routes.",
            "A small, leafy plant that's bigger than grass but smaller than a tree.",
            "A colorful insect with delicate wings that starts life as a caterpillar.",
            "A spiky desert plant that stores water and rarely needs a drink.",
            "A sweet, fluffy dessert that makes every celebration more delicious.",
            "A device that does math problems faster than you can blink.",
            "A chart showing all the days, weeks, and months of the year.",
            "A desert animal with humps that can go days without drinking water.",
            "A device that captures memories and freezes moments in time.",
            "A pattern that helps soldiers and animals blend into their surroundings.",
            "A controlled flame where people gather to roast marshmallows and tell stories.",
            "A waxy stick with a wick that provides gentle, flickering light.",
            "A large gun that shoots heavy metal balls with a thunderous boom.",
            "A narrow boat that you paddle through calm rivers and lakes.",
            "A four-wheeled vehicle that takes you places faster than walking.",
            "An orange root vegetable that helps you see in the dark.",
            "A large stone fortress where kings and queens once lived.",
            "A furry pet that purrs, catches mice, and knocks things off tables.",
            "A spinning device overhead that keeps you cool on hot days.",
            "A portable device that lets you talk to anyone, anywhere in the world.",
            "A large stringed instrument that you play while sitting down.",
            "A piece of furniture with four legs where you sit down to rest.",
            "An elegant hanging light fixture made of crystals and sparkling glass.",
            "A sacred building with a tall spire where people go to pray.",
            "A perfectly round shape with no corners or edges.",
            "A long, black woodwind instrument with silver keys and a single reed.",
            "A device with hands or numbers that tells you what time it is.",
            "A fluffy white mass floating in the sky that sometimes brings rain.",
            "A small container that holds the magical brown liquid that wakes people up.",
            "A navigation tool with a needle that always points north.",
            "An electronic machine that processes information and connects you to the internet.",
            "A sweet, round treat that's perfect with a glass of cold milk.",
            "An insulated box that keeps your drinks cold during picnics and camping trips.",
            "A soft, cushioned seat where families gather to watch movies and relax.",
            "A large farm animal that gives us milk and says 'moo'.",
            "A sideways-walking sea creature with big claws and a hard shell.",
            "A colorful wax stick that children use to create artwork and color pictures.",
            "A large reptile with sharp teeth that lurks in swamps and rivers.",
            "A golden headpiece decorated with jewels that royalty wears on their head.",
            "A floating hotel that takes passengers on luxurious ocean vacations.",
            "A small container with a handle that holds your favorite hot or cold beverage.",
            "A precious stone that sparkles brilliantly and is harder than any other material.",
            "A kitchen appliance that cleans your plates and utensils with hot, soapy water.",
            "A springy platform that launches swimmers high into the pool below.",
            "Man's best friend who wags their tail, fetches sticks, and gives unconditional love.",
            "An intelligent sea mammal that leaps through waves and performs amazing tricks.",
            "A sweet, round pastry with a hole in the middle, often covered in glaze.",
            "A hinged barrier that opens and closes to let you enter or exit rooms.",
            "A mythical creature with wings that breathes fire and hoards treasure.",
            "A bedroom furniture piece with drawers where you store your clothes.",
            "A spinning tool that makes holes in wood, metal, or walls.",
            "Musical instruments you hit with sticks to create rhythm and beats.",
            "A water bird with webbed feet that quacks and waddles around ponds.",
            "A weight-lifting equipment with a handle in the middle and heavy ends.");

    private static List<String> ITEMS = Arrays.asList(
            "airplane", "alarm clock", "ambulance", "angel", "animal migration", "ant",
            "anvil", "apple", "arm", "asparagus", "axe", "backpack", "banana", "bandage",
            "barn", "baseball", "baseball bat", "basket", "basketball", "bat", "bathtub",
            "beach", "bear", "beard", "bed", "bee", "belt", "bench", "bicycle", "binoculars",
            "bird", "birthday cake", "blackberry", "blueberry", "book", "boomerang",
            "bottlecap", "bowtie", "bracelet", "brain", "bread", "bridge", "broccoli",
            "broom", "bucket", "bulldozer", "bus", "bush", "butterfly", "cactus", "cake",
            "calculator", "calendar", "camel", "camera", "camouflage", "campfire", "candle",
            "cannon", "canoe", "car", "carrot", "castle", "cat", "ceiling fan",
            "cell phone","cello", "chair", "chandelier", "church", "circle", "clarinet", "clock",
            "cloud", "coffee cup", "compass", "computer", "cookie", "cooler", "couch",
            "cow", "crab", "crayon", "crocodile", "crown", "cruise ship", "cup", "diamond",
            "dishwasher", "diving board", "dog", "dolphin", "donut", "door", "dragon",
            "dresser", "drill", "drums", "duck", "dumbbell");
    private static final String PREF_NAME = "doodle_prefs";
    private static final String KEY_DISCOVERED = "discovered";
    private SharedPreferences prefs;
    private int lastIndex = -1;

    public DoodleProgressRepository(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public String[] getAllItemsArray() {
        return ITEMS.toArray(new String[0]);
    }

    public Set<String> getDiscovered() {
        return new HashSet<>(prefs.getStringSet(KEY_DISCOVERED, new HashSet<>()));
    }

    public void addDiscovered(String item) {
        Set<String> discovered = getDiscovered();
        discovered.add(item);
        prefs.edit().putStringSet(KEY_DISCOVERED, discovered).apply();
    }

    public boolean isDiscovered(String item) {
        return getDiscovered().contains(item);
    }

    public List<String> getUndiscovered() {
        Set<String> discovered = getDiscovered();
        List<String> result = new ArrayList<>();
        for (String item : ITEMS) {
            if (!discovered.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public String getRandomUndiscoveredItem() {
        List<String> undiscovered = getUndiscovered();
        if (undiscovered.isEmpty()) return null;

        lastIndex++;
        if (lastIndex >= undiscovered.size()) lastIndex = 0;
        return undiscovered.get(lastIndex);
    }

    public String getHintForItem(String item) {
        int index = ITEMS.indexOf(item);
        if (index != -1) {
            return HINTS.get(index);
        }
        return "";
    }

    public void resetDiscoveredItems() {
        prefs.edit().remove(KEY_DISCOVERED).apply();
    }

    public boolean isEverythingDiscovered() {
        return prefs.getBoolean("all_discovered", false);
    }

    public void markEverythingDiscovered() {
        prefs.edit().putBoolean("all_discovered", true).apply();
    }

}
