alter table agent_listing
add constraint FKm59na7la8cnr7fjpab9sjvo7t foreign key (listing_id) references listing;

alter table agent_listing
add constraint FK29wunme00qrfdsqwmfjlnl4t7 foreign key (user_id) references app_user;

alter table listing
add constraint FK6vpdsuxgywcnh114rvw4ax7cy foreign key (creator) references app_user;

alter table offer
add constraint FKqsbrjj3aajgd9jmfelwo1nps6 foreign key (listing_id) references listing;

alter table offer
add constraint FKo78pgqyk6340o1digyxpxmh7y foreign key (user_id) references app_user;

alter table review
add constraint FKko6tkmagj4fgefq5pkvw8k9wa foreign key (creator) references app_user;

alter table review
add constraint FKc7y0l3wac4n2ewm6a2uecd54c foreign key (user_id) references app_user;