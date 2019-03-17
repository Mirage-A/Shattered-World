# Shattered World - Client (WIP)

## Мануал по установке

Команды для установки на убунту:

1. Устанавливаем Java 8

    sudo add-apt-repository ppa:webupd8team/java
    
    sudo apt install oracle-java8-set-default
    
2. Клонируем репозиторий

    git clone https://github.com/Mirage-A/Shattered-World.git
    
    cd Shattered-World
    
Далее два пути: полная сборка проекта или запуск уже собранного jar-ника

3.1. Для самоотверженных (переключаемся на ветку без android-а, а то еще придется Android SDK устанавливать)

    git checkout light
    
    chmod +x ./gradlew
    
    ./gradlew wrapper :desktop:run
    
3.2. Альтернатива, если не хочется тратить кучу времени на билд с нуля (выполняем команду в master-ветке).

    java -jar "SW - Game.jar"

![](art.png)

Shattered World is an RPG that i develop in my free time

There is A LOT of work to be done to make my dreams come true, but one can always try what is done at the moment (currently nothing) by downloading game [here](https://mirage-a.github.io) and launching downloaded .jar file with Java

And yes, i draw everyfin in Paint ;)

One day i'll complete it :) (if i wont be expelled earlier)
