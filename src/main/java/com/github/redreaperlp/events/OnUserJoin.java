package com.github.redreaperlp.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OnUserJoin extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();
        System.out.println("User joined: " + user.getName());
        MessageChannel channel = user.openPrivateChannel().complete();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(e.getUser().getName(), null, e.getUser().getEffectiveAvatarUrl());
        eb.setDescription("Welcome to the server! Please read the rules and have fun!");
        eb.setColor(0xa80000);
        eb.addField("Rules", "1. No spamming\n2. No NSFW content\n3. No advertising", true);
        eb.setThumbnail("https://cdn.discordapp.com/attachments/1060601917878829226/1060602918824312924/image.png");
        eb.setFooter("Bot made by RedReaperLP", "https://cdn.discordapp.com/attachments/1060601917878829226/1060602918824312924/image.png");
        channel.sendMessageEmbeds(eb.build()).queue();
    }
}
