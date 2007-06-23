find "$1" | grep -v svn | grep .java | \
while read file
do
	if [ -d $file ]
	then
		echo \\subsubsection*{`echo $file | sed s/[^a-z]*// | sed s/.java// | sed s/[/]/\./g`}	
	else
		echo \\lstinputlisting[caption=`echo $file | sed s/[^a-z]*// | sed s/.java// | sed s/[/]/\./g`]{../org.opencad.core/src/$file}
	fi
done
