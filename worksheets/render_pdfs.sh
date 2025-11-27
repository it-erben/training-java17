#!/bin/bash

# 1. Common header: Ensure headings are followed by text (prevent orphans)
# We use 'needspace' to check if there is enough room for the heading + ~3-4 lines of text.
cat << EOF > common.tex
\usepackage{needspace}
% Check for ~7-8 lines of space (header + spacing + 3 lines text)
\let\oldsection\section
\renewcommand{\section}{\needspace{8\baselineskip}\oldsection}
\let\oldsubsection\subsection
\renewcommand{\subsection}{\needspace{8\baselineskip}\oldsubsection}
\let\oldsubsubsection\subsubsection
\renewcommand{\subsubsection}{\needspace{8\baselineskip}\oldsubsubsection}
EOF

# 2. Code No-Break header: Prevent page breaks inside code blocks
cat << EOF > code_nobreak.tex
\usepackage{etoolbox}
\BeforeBeginEnvironment{Shaded}{\begin{minipage}{\linewidth}}
\AfterEndEnvironment{Shaded}{\end{minipage}}
EOF

# Iterate over all .md files in the current directory
for file in *.md; do
    # Extract the filename without extension
    filename=$(basename -- "$file")
    filename="${filename%.*}"
    
    echo "Rendering $file to ${filename}.pdf..."
    
    # Determine which headers to include
    # Always include common.tex
    HEADERS="-H common.tex"
    
    # Include code_nobreak.tex for everything EXCEPT 03-streams
    if [ "$filename" != "03-streams" ]; then
        HEADERS="$HEADERS -H code_nobreak.tex"
    fi
    
    pandoc "$file" \
        -o "${filename}.pdf" \
        --pdf-engine=xelatex \
        --highlight-style=tango \
        -V geometry:margin=2cm \
        $HEADERS
        
    if [ $? -eq 0 ]; then
        echo "Successfully created ${filename}.pdf"
    else
        echo "Error rendering $file"
    fi
done

# Clean up
rm common.tex code_nobreak.tex