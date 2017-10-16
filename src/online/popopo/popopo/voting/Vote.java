package online.popopo.popopo.voting;

import online.popopo.api.message.Guideable;
import online.popopo.api.message.Notice;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Vote {
    private final Player owner;
    private final String title;
    private final List<Choice> choices;

    public Vote(Player owner, String title, String[] items) {
        this.owner = owner;
        this.title = title;
        this.choices = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            this.choices.add(new Choice(items[i]));
        }
    }

    public Player getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void voteChoice(Notice n, int i) {
        String name = choices.get(i).getName();
        String s = "\"" + name + "\"";

        n.good("Done", "Voted to " + s);
        choices.get(i).count++;
    }

    public void showResult(Notice n) {
        String s = "\"" + title + "\"";
        int rank = 0;
        int last = -1;

        choices.sort(Choice::compareTo);
        n.info("Info", "Result voting of " + s);

        for (Choice c : choices) {
            if (c.count != last) rank++;

            String name = Integer.toString(rank);

            n.guide(name, c);
            last = c.count;
        }
    }

    class Choice implements Guideable, Comparable<Choice> {
        private final String name;

        private int count = 0;

        Choice(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getLoreTitle() {
            return name;
        }

        @Override
        public List<String> getLore() {
            List<String> list = new ArrayList<>();

            list.add(count + " player");

            return list;
        }

        @Override
        public int compareTo(Choice o) {
            return o.count - count;
        }
    }
}
