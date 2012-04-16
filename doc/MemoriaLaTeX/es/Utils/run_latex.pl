#!/usr/bin/perl -w

#####################################################
#                                                   #
# Script para compilar la plantilla de PFC de Itsas #
#                                                   #
#####################################################

use strict;

#
# Chequear que el fichero principal existe
#
my $f = 'Main'; # Nombre de fichero principal (puedes cambiarlo, por supuesto)
die "File $f.tex does not exist!\n" unless (-f "$f.tex");

#
# Sacar tamaño del libro del .tex
#
my $psize = 'a4';                                          # Por defecto A4
$psize    = 'b5' if (`grep documentclass $f.tex` =~ /b5/); # B5, si especificado

#
# Hasta donde queremos compilar. Deben especificarse números del 1 al 4, y tantos
# como opciones se quieran (p.e. "24" = generar PS y verlo, "23" = generar PS y PDF
# pero no ver ninguno).
#
my $mode  = $ARGV[0] || 3; # Modo:
                           # 1 -> Solo .dvi
			   # 2 -> También .ps
			   # 3 -> También .pdf
			   # 4 -> Ver (PDF si posible, si no PS)

#
# Chequear que el modo está bien
#
die "El argumento '$mode' contiene opciones de compilacion incorrectas (1, 2, 3 o 4)!\n" unless ($mode =~ /^[1234]+$/);

# Si el único argumento es '4', asumir '3' también:
$mode = '34' if ($mode == 4);

#
# Definir visores de PostScript y PDF
#
my $seeps  = 'gv';
my $seepdf = 'acroread';

#
# Otras variables
#
my $tmp    = 'tmp';
my $outps  = $f.'.ps';
my $outpdf = $f.'.pdf';

#
# Creamos la lista de qué hacer
#
my @list;
push(@list,"Utils/extract_lyx.pl");                                                 # si hay ficheros LyX presentes, convertirlos a LaTeX
push(@list,"cp -f $f.tex $tmp.tex");                                                # copia temporal de fichero a compilar
push(@list,"latex $tmp");                                                           # correr latex
push(@list,"bibtex $tmp");                                                          # correr bibtex
push(@list,"latex $tmp");                                                           # segunda compilación, para referencias cruzadas
push(@list,"latex $tmp");                                                           # correr latex otra vez
push(@list,"dvips -t $psize $tmp.dvi -o $tmp.ps > /dev/null") unless ($mode == 1);  # convertir DVI en PS
push(@list,"mv $tmp.dvi Main.dvi") if ($mode =~ /1/);                               # preservar DVI
push(@list,"ps2pdf $tmp.ps") if ($mode =~ /3/ );                                    # convertir PS en PDF
push(@list,"mv -f $tmp.ps  $outps")  if ($mode =~ /2/);                             # salvar copia del PS en $outps
push(@list,"mv -f $tmp.pdf $outpdf") if ($mode =~ /3/);                             # salvar copia del PDF en $outpdf
push(@list,"$seeps $outps > /dev/null &") if ($mode =~ /4/ and $mode !~ /3/);       # abrir PS y seguir
push(@list,"$seepdf $outpdf &")           if ($mode =~ /4/ and $mode =~ /3/);       # abrir PDF y seguir
push(@list,"rm -f $tmp.*");                                                         # borrar basura

#
# Para cronometrar los pasos
#
my $hasi = time(); # fecha +%s de la hora de comienzo
my $oldt = $hasi;
my $str  = '';     # string con todos los pasos

#
# Ejecutar todos los pasos en la lista
#
foreach (@list)
{
  my $do = $_;

  my $ok  = 'OK';          # Si el comando se ejecutó correctamente
  my $sys = system "$do";  # ejecutar comando, y coger el estatus de salida
  my $t   = time();
  my $dt  = $t-$oldt;      # segundos que tarda este paso
  my $dt0 = $t-$hasi;      # segundos que tarda hasta este paso
  $oldt   = $t;            # la hora en que termina este paso
  if ($sys) { $ok = 'KO' } # no-OK si el estatus de salida no fue 0

  #
  # Guardar info para imprimir en pantalla luego
  #
  $str .= sprintf "%-50s   [ %2s ] %6is %6is\n",$do,$ok,$dt,$dt0;

  last if $sys;            # salir si el último comando falló
};

#
# Imprimir resumen
#
printf "\nRESUMEN [ %1s ]:\n\n%-50s %8s %7s %7s\n\n%1s\n",$f,'Comando','Status','Paso','Total',$str;
