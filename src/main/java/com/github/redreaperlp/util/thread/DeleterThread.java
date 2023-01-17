package com.github.redreaperlp.util.thread;

import com.github.redreaperlp.enums.CommandEn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DeleterThread implements Runnable {
    public static List<String> ids = new ArrayList<>();
    private SlashCommandInteractionEvent e;
    private InteractionHook hook;
    private Message message;
    private int delay;
    private int action;

    Object lock = new Object();

    public DeleterThread(SlashCommandInteractionEvent e) {
        this.hook = e.getHook();
        this.action = 0;
        this.e = e;
    }

    public DeleterThread(Message hook, int delay, SlashCommandInteractionEvent e) {
        this.e = e;
        this.message = hook;
        this.delay = delay;
        this.action = 1;
    }

    @Override
    public void run() {
        synchronized (lock) {
            if (action == 0) {
                boolean clearUserMSG = false;

                OptionMapping userOption = e.getOption(CommandEn.CLEAR.optionName(1));
                if (userOption != null) {
                    clearUserMSG = true;
                }

                int toClear = e.getOption(CommandEn.CLEAR.optionName(0)).getAsInt();
                MessageHistory his = e.getChannel().getHistoryAround(e.getChannel().getLatestMessageId(), 100).complete();
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
                e.getChannel().purgeMessages(messagesToDel);
                new DeleterThread(e.getHook().sendMessageEmbeds(new EmbedBuilder().setTitle("Cleared " + cleared + " messages!").setColor(0x00ff00).build()).complete(), 5, e).run();
            } else if (action == 1) {
                e.getChannel().getHistory().retrievePast(1).queue(messages -> {
                    if (messages.size() > 0) {
                        Message message = messages.get(0);
                        ids.add(message.getId());
                        CompletableFuture.delayedExecutor(delay, TimeUnit.SECONDS).execute(() -> {
                            message.delete().queue();
                            ids.remove(message.getId());
                        });
                    }
                });
            }
        }
    }

    public boolean isReply(Message msg) {
        for (String id : DeleterThread.ids) {
            if (msg.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
