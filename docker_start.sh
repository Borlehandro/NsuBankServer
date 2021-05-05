#sudo /usr/lib/jvm/java-16-oracle/bin/java \
#-Dmaven.multiModuleProjectDirectory=/home/brainworm/Servers/NsuBankSystemServer \
#-Dmaven.home=/home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/plugins/maven/lib/maven3 \
#-Dclassworlds.conf=/home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/plugins/maven/lib/maven3/bin/m2.conf \
#-Dmaven.ext.class.path=/home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/plugins/maven/lib/maven-event-listener.jar \
#-javaagent:/home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/lib/idea_rt.jar=44359:/home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/bin \
#-Dfile.encoding=UTF-8 -classpath /home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/plugins/maven/lib/maven3/boot/plexus-classworlds.license:/home/brainworm/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.6693.111/plugins/maven/lib/maven3/boot/plexus-classworlds-2.6.0.jar \
#org.codehaus.classworlds.Launcher \
#-Didea.version=2021.1 \
#install -DskipTests

sudo docker build -t bank-server .
sudo docker run --rm --name=bank-server --net=bank_network -p 8080:8080 bank-server