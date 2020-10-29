# JMindMaps

## Overview

JMindMaps is a simple application that allows creating, saving and loading
mind maps in a custom XML format.

## Guide

When you open JMindMaps a window appears with the canvas on the left and the
toolbar on the right.

### Inserting new nodes

You can create new nodes of the map by double clicking in the canvas area, or
by selecting "Add node" option on the toolbar, and then click on the canvas. 
Remember to go back to "Select node" mode, if you want to operate on the items on 
canvas.

### Editing nodes title, adding an image and description to a node.

After you have inserted few nodes you can change their title, add description
or image to a node. To edit node title simply click on the placeholder and enter the
node title text. 
Adding an image or a description is done by hovering the mouse cursor over a node and
selecting appropriate option if you want to add image or description. You can click
the image button to add image. You will be then prompted to open an image from disk.
When adding a description a placeholder will appear. You can edit it the same way
you edit the node's title.

### Making connections.

To make a connetion between existing nodes just hover over a node you want, press
the mouse over the chain icon and drag it to another node, then release the mouse button.

You can also create new nodes using the chain button. If you click and drag this button 
far enough from the node the cursor will change into crosshair. Releasing a button will
yield a new node attached to the one from which the drag came from.

### Using existing images.

Every image you add to a node will be stored using absolute path to the filesystem.
The images are stored in the toolbar on the right in the "Images" tab. You can
use these images to assign them to different nodes. Just select the node you want
the image to change and choose an image from the toolbar "Images" tab. Here you can
also browse for more images if you want to change the selected node image.

### More

In order to access existing nodes on the canvas you can switch to the "Nodes" tab
on the toolbar. Clicking a node will cause the canvas to move to the clicked node. 

There is also a possibility to undo and redo an action. Simply press CTRL + Z to undo
an action and CTRL + SHIFT + Z to redo an action. Take note this won't change node
text. The redo and undo feature only changes the layout of the map - the undo work
on events such as: new node addition, node deletion, node reposition, image change, 
and connection addition and removal. 