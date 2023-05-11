create index IDXca699byqpy39i3fkohu5ya69m on app_user (name);
    create index IDX1j9d9a06i600gd43uu3km82jw on app_user (email);
    create index IDXrpbruh8sps526x8373jd716jx on listing (name);
    create index IDXd92kw6gj1wcv80grp8ysuvnu3 on listing (rooms);
    create index IDXcjw24a1uw5immhds1nxxc5ejo on offer (user_id);
    create index IDXlr312qtcf9n8342h7stm46it0 on offer (listing_id);
    create index IDXjdnb3cnup5vq6opj19kbm8w4d on review (user_id);
    create index IDXiywhwua3y0e476mdg657v57gd on user_login (username, password, is_active);
