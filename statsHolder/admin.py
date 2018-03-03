# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin

from statsHolder.models import statsHolder, User, Team, Fixture, Player, Action, Substitutions 

admin.site.register(statsHolder)
admin.site.register(User)
admin.site.register(Team)
admin.site.register(Fixture)
admin.site.register(Player)
admin.site.register(Action)
admin.site.register(Substitutions)
# Register your models here.
