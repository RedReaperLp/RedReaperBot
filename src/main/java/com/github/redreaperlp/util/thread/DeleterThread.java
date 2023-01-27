package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.enums.CommandEn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DeleterThread implements Runnable {
    public static List<String> ids = new ArrayList<>();
    public static List<String> runningIds = new ArrayList<>();
    private final SlashCommandInteractionEvent e;
    private final InteractionHook hook;
    private int cleared;
    private final int action;

    Object lock = new Object();

    public DeleterThread(SlashCommandInteractionEvent e) {
        this.hook = e.getHook();
        this.action = 0;
        this.e = e;
    }

    @Override
    public void run() {
        synchronized (lock) {
            boolean shouldReset = true;
            if (!runningIds.contains(e.getChannel().getId())) {
                boolean clearUserMSG = false;
                boolean zero = false;
                int toClear = e.getOption(CommandEn.CLEAR.optionName(0)).getAsInt();
                if (toClear == 0) {
                    shouldReset = false;
                    runningIds.add(e.getChannel().getId());
                    MessageEmbed equalsZero = new EmbedBuilder().setTitle("\"0\" is not a operatable number!").setThumbnail("https://discordapp.com/channels/591985618469257218/1060601917878829226/1066511720383778876").setColor(0xffff00).build();
                    e.getHook().sendMessageEmbeds(equalsZero).complete()
                            .addReaction(Emoji.fromFormatted("⁉️")).queue();
                    zero = true;
                }
                if (!zero) {
                    OptionMapping userOption = e.getOption(CommandEn.CLEAR.optionName(1));
                    if (userOption != null) {
                        clearUserMSG = true;
                    }

                    MessageHistory his = e.getChannel().getHistoryBefore(e.getInteraction().getId(), 100).complete();
                    List<Message> messagesHis = his.getRetrievedHistory();
                    List<Message> messagesToDel = new ArrayList<>();
                    int cleared = 0;
                    if (clearUserMSG) {
                        for (Message msg : messagesHis) {
                            if (!isReply(msg)) {
                                if (msg.getAuthor().equals(userOption.getAsUser())) {
                                    messagesToDel.add(msg);
                                    cleared++;
                                }
                                if (cleared == toClear) {
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Message msg : messagesHis) {
                            if (!isReply(msg)) {
                                if (!msg.isEphemeral()) {
                                    cleared++;
                                }
                                messagesToDel.add(msg);
                                if (cleared == toClear) {
                                    break;
                                }
                            }
                        }
                    }
                    if (cleared == 0) {
                        shouldReset = false;
                        runningIds.add(e.getChannel().getId());
                        e.getHook().sendMessageEmbeds(new EmbedBuilder().setTitle("There were no messages to Clear").setColor(0xffff00).build()).complete()
                                .addReaction(Emoji.fromFormatted("⁉️")).queue();
                    } else {
                        runningIds.add(e.getChannel().getId());
                        List<CompletableFuture<Void>> f = e.getChannel().purgeMessages(messagesToDel);
                        for (CompletableFuture<Void> future : f) {
                            future.join();
                        }
                        e.getHook().sendMessageEmbeds(new EmbedBuilder().setTitle("Cleared " + cleared + " messages!").setColor(0x00ff00).build()).complete()
                                .addReaction(Emoji.fromFormatted("✅")).queue();
                    }
                }
            } else {
                shouldReset = false;
                e.getHook().sendMessageEmbeds(new EmbedBuilder().setTitle("There is already a Delete request Running!").setColor(0xff0000).build()).complete()
                        .addReaction(Emoji.fromFormatted("⁉️")).queue();
            }
            boolean finalShouldReset = shouldReset;
            e.getChannel().getHistory().retrievePast(10).queue(messages -> {
                if (messages.size() > 0) {
                    int index = 0;
                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i).getId().equals(e.getInteraction().getId())) {
                            index = i;
                        }
                    }
                    ids.add(messages.get(index).getId());
                    int finalIndex = index;
                    CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
                        messages.get(finalIndex).delete().queue();
                        if (finalShouldReset) {
                            ids.remove(messages.get(0).getId());
                        }
                        runningIds.remove(e.getChannel().getId());
                    });
                }
            });
        }
    }


    public boolean isReply(Message msg) {
        return ids.contains(msg.getId());
    }
}
