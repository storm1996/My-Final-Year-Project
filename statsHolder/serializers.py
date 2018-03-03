from statsHolder.models import statsHolder, User, Team, Fixture, Player, Action, Substitutions
from rest_framework import serializers

class statsHolderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = statsHolder
        fields = ('id', 'playerName', 'gearNumber', 'twoPointer')

class userSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('user_id', 'username', 'password')

class teamSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Team
        fields = ('team_id', 'team_name', 'league', 'home_location', 'user_id')

class fixtureSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Fixture
        fields = ('fixture_id', 'home_team', 'away_team', 'fixture_date', 'venue')

class playerSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Player
        fields = ('player_id', 'player_name', 'player_number', 'team_id')

class actionSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Action
        fields = ('action_id', 'action_type', 'action_timestamp', 'player_id', 'fixture_id')

class substitutionsSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Substitutions
        fields = ('sub_id', 'sub_timestamp', 'sub_in', 'sub_out', 'player_id', 'fixture_id')
