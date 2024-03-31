package org.nwolfhub.util;

import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.PreparedMessage;
import org.nwolfhub.database.model.Section;
import org.nwolfhub.database.model.Unit;
import org.nwolfhub.database.repositories.FieldRepository;
import org.nwolfhub.database.repositories.MessagesRepository;
import org.nwolfhub.database.repositories.SectionRepository;
import org.nwolfhub.database.repositories.UnitRepository;
import org.nwolfhub.telegram.UpdateHandler;
import org.springframework.stereotype.Component;

import javax.management.Query;
import java.util.*;

/**
Anything between "$(" and ")" is considered a query to be processed
Inside a query, you can execute commands that would override your inline output or mention other stuff in your message.

Commands:

1) search (aliases: docs)
 args: type (repository), query
 usage: $([search|docs] [fields|sections|units] (name)
 demo: $(search units sendMessage)
 outputs: brief documentation of what you've requested


Capture group works by typing backslash and the name of type, capture mode and name of thing to be captured.
 For instance, $(\fsmigrate_to_chat_id) would output migrate_to_chat_id written in inline font. F means field, S means simple.
 $(\fdmigrate_to_chat_id) would output description of migrate_to_chat_id field from the bot api (d means description), ($\ftmigrate_to_chat_id) would output Integer.
 **/
@Component
public class QueryProcessor {
    private final FieldRepository fieldRepository;
    private final SectionRepository sectionRepository;
    private final UnitRepository unitRepository;
    private final Random random = new Random();

    public QueryProcessor(FieldRepository fieldRepository, SectionRepository sectionRepository, UnitRepository unitRepository, MessagesRepository messagesRepository) {
        this.fieldRepository = fieldRepository;
        this.sectionRepository = sectionRepository;
        this.unitRepository = unitRepository;
    }

    public QueryResult processQuery(String query) {
        String[] split = query.split("");
        if(split[0].equals("\\")) {
            int next = 3;
            String type = split[1];
            String mode = split[2];
            if(!type.matches("[fsu]")) {type="f"; next-=1;}
            if(!mode.matches("[sdt]")) {mode = "s"; next-=1;}
            String innerQuery = query.substring(next);
            switch (type) {
                case "f" -> {
                    Optional<Field> requested = fieldRepository.findFieldByNameIgnoreCase(innerQuery);
                    if(requested.isEmpty()) {
                        throw new IllegalArgumentException("Field " + innerQuery + " not found");
                    }
                    switch (mode) {
                        case "s" -> {
                            return new QueryResult("_" + replace(requested.get().name) + "_", false);
                        } case "d" -> {
                            return new QueryResult("\n>" + replace(requested.get().description) + "**", false);
                        } case "t" -> {
                            return new QueryResult("__" + replace(requested.get().type) + "__", false);
                        }
                    }
                }
                case "s" -> {
                    Optional<Section> requested = sectionRepository.findSectionByNameIgnoreCase(innerQuery);
                    if(requested.isEmpty()) {
                        throw new IllegalArgumentException("Field " + innerQuery + " not found");
                    }
                    switch (mode) {
                        case "s" -> {
                            return new QueryResult("[" +requested.get().getName() + "](https://core.telegram.org/bots/api#" + requested.get().getName().toLowerCase().replace(" ", "-") + ")", false);
                        } case "d" -> {
                            return new QueryResult("\n>" + replace(requested.get().getName()) + "**", false);
                        } case "t" -> {
                            return new QueryResult("_" + replace(requested.get().getName()) + "_", false);
                        }
                    }
                }
                case "u" -> {
                    Optional<Unit> requested = unitRepository.findUnitByNameIgnoreCase(innerQuery);
                    if(requested.isEmpty()) {
                        throw new IllegalArgumentException("Field " + replace(innerQuery) + " not found");
                    }
                    switch (mode) {
                        case "s" -> {
                            return new QueryResult("[" +requested.get().getName() + "](https://core.telegram.org/bots/api#" + requested.get().getName().toLowerCase().replace(" ", "-") + ")", false);
                        } case "d" -> {
                            return new QueryResult("\n>" + replace(requested.get().description) + "**", false);
                        } case "t" -> {
                            throw new IllegalArgumentException("Type t is not supported for unit search");
                        }
                    }
                }
            }
        } else {
            split = query.split(" ");
            if(split[0].equals("search") || split[0].equals("docs")) {
                int begin = 2;
                String type = split[1];
                if(!type.matches("(fields|sections|units)")) {type = "units"; begin-=1;}
                String toSearch = String.join(" ", Arrays.copyOfRange(split, begin, split.length));
                switch (type) {
                    case "units" -> {
                        List<Unit> found = unitRepository.findTop5ByNameLikeIgnoreCase("%" + toSearch + "%");
                        List<PreparedMessage> preparedMessages = new ArrayList<>();
                        for(Unit unit:found) {
                            StringBuilder fields = new StringBuilder();
                            List<Field> fieldList = unit.fields;
                            for(Field field:fieldList) {
                                if(!fields.isEmpty()) {
                                    fields.append("\n\n");
                                }
                                fields.append(field.name).append(": ").append(field.type).append("\n").append(field.description);
                                if(field.required!=null) fields.append("\nrequired: ").append(field.required);
                            }
                            replaceSpecials(preparedMessages, unit.getName(), unit.description + (fields.isEmpty()?"":"\n\nfields:\n" + fields));
                        }
                        return new QueryResult(preparedMessages);
                    }
                    case "fields" -> {
                        List<Field> found = fieldRepository.findTop5ByNameLikeIgnoreCase("%" + toSearch + "%");
                        List<PreparedMessage> preparedMessages = new ArrayList<>();
                        for(Field unit:found) {
                            replaceSpecials(preparedMessages, unit.getName(), unit.description);
                        }
                        return new QueryResult(preparedMessages);
                    }
                    case "sections" -> {
                        List<Section> found = sectionRepository.findTop5ByNameLikeIgnoreCase("%" + toSearch + "%");
                        List<PreparedMessage> preparedMessages = new ArrayList<>();
                        for(Section unit:found) {
                            replaceSpecials(preparedMessages, unit.getName(), unit.getName());
                        }
                        return new QueryResult(preparedMessages);
                    }
                }
            }
        }
        throw new IllegalStateException("Unexpected ending");
    }

    private void replaceSpecials(List<PreparedMessage> preparedMessages, String name, String description) {
        preparedMessages.add(
                new PreparedMessage(
                        random.nextLong(),
                        name,
                        "*Documentation about __" + name + "__*:\n\n" + replace(description),
                        1L,
                        false
                )
        );
    }
    private String replace(String description) {
        String text = description;
        text = text.replace("\\", "\\\\");
        for(char c: UpdateHandler.toEscape) {
            text = text.replace(String.valueOf(c), "\\" + c);
        }
        return text;
    }

    public static class QueryResult {
        public String result;
        public Boolean override;
        public List<PreparedMessage> preparedMessages = new ArrayList<>();

        public QueryResult(List<PreparedMessage> preparedMessages) {
            this.preparedMessages = preparedMessages;
            this.override = true;
        }

        public QueryResult(String result, Boolean override) {
            this.result = result;
            this.override = override;
        }

        public QueryResult(String result) {
            this.result = result;
            this.override = false;
        }

        public String getResult() {
            return result;
        }

        public QueryResult setResult(String result) {
            this.result = result;
            return this;
        }

        public Boolean getOverride() {
            return override;
        }

        public QueryResult setOverride(Boolean override) {
            this.override = override;
            return this;
        }
    }
}
