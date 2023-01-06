package com.github.redreaperlp.events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class onUserJoin extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();
        System.out.println("User joined: " + user.getName());
        MessageChannel channel = user.openPrivateChannel().complete();
        channel.sendMessage("Welcome to the server!").queue();
    }
}
