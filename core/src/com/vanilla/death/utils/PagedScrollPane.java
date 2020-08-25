package com.vanilla.death.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class PagedScrollPane extends ScrollPane {

    private boolean wasPanDragFling = false;

    private Table content;

    private ArrayMap<Float, Integer> pageCoordinates;
    private Runnable actionInScroll;
    public static int currentPage = 0;
    private boolean isWasOnLeftEdge = true, isWasOnRightEdge = false;

    public PagedScrollPane () {
        super(null);
        setup();
    }

    @SuppressWarnings("unused")
    public PagedScrollPane (Skin skin) {
        super(null, skin);
        setup();
    }

    @SuppressWarnings("unused")
    public PagedScrollPane (Skin skin, String styleName) {
        super(null, skin, styleName);
        setup();
    }

    @SuppressWarnings("unused")
    public PagedScrollPane (Actor widget, ScrollPaneStyle style) {
        super(null, style);
        setup();
    }

    private void setup() {
        pageCoordinates = new ArrayMap<Float, Integer>();
        actionInScroll = new Runnable() {
            @Override
            public void run() {
                //Empty action
            }
        };
        content = new Table();
        content.defaults().space(50);
        super.setWidget(content);
    }

    @SuppressWarnings("unused")
    public void addPages (float width, Actor... pages) {
        for (Actor page : pages) {
            content.add(page).expandY().fillY();
            pageCoordinates.put(width * pageCoordinates.size, pageCoordinates.size);
        }
    }

    @SuppressWarnings("unused")
    public void addPage (Actor page, float width) {
        content.add(page).expandY().fillY();
        pageCoordinates.put(width * pageCoordinates.size, pageCoordinates.size);
    }

    @SuppressWarnings("unused")
    public void removePage(Actor page) {
        content.removeActor(page);
        pageCoordinates.removeValue(pageCoordinates.size, true);
        if (currentPage > pageCoordinates.size) currentPage--;
    }

    public void setActionInScroll(Runnable actionInScroll) {
        this.actionInScroll = actionInScroll;
    }

    @Override
    public void act (float delta) {
        super.act(delta);
        if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
            wasPanDragFling = false;
            scrollToPage();
        } else {
            if (isPanning() || isDragging() || isFlinging()) {
                wasPanDragFling = true;
                if (getScrollX() <= 0 && !isWasOnLeftEdge) {
                    currentPage = 0;
                    isWasOnLeftEdge = true;
                    isWasOnRightEdge = false;
                    actionInScroll.run();
                }
                if (getScrollX() >= pageCoordinates.getKeyAt(pageCoordinates.size - 1) && !isWasOnRightEdge) {
                    currentPage = pageCoordinates.size - 1;
                    isWasOnRightEdge = true;
                    isWasOnLeftEdge = false;
                    actionInScroll.run();
                }
            }
        }
    }

    /*@Override
    public void setWidget (Actor widget) {
        //throw new UnsupportedOperationException("Use PagedScrollPane#addPage.");
    }*/

    @Override
    public void setWidth (float width) {
        super.setWidth(width);
        if (content != null) {
            Cell[] array = content.getCells().toArray(Cell.class);
            for (Cell cell : array) {
                cell.width(width);
            }
            content.invalidate();
        }
    }

    public void setPageSpacing (float pageSpacing) {
        if (content != null) {
            content.defaults().space(pageSpacing);
            Cell[] array = content.getCells().toArray(Cell.class);
            for (Cell cell : array) {
                cell.space(pageSpacing);
            }
            content.invalidate();
        }
    }

    private void scrollToPage () {
        final float width = getWidth();
        final float scrollX = getScrollX();
        final float maxX = getMaxX();

        if (scrollX >= maxX || scrollX <= 0) return;

        Array<Actor> pages = content.getChildren();
        float pageX = 0;
        float pageWidth = 0;
        if (pages.size > 0) {
            Actor[] array = pages.toArray(Actor.class);
            for (Actor a : array) {
                pageX = a.getX();
                pageWidth = a.getWidth();
                if (scrollX < (pageX + pageWidth * 0.5)) {
                    break;
                }
            }
            float nextPage = MathUtils.clamp(pageX - (width - pageWidth) / 2, 0, maxX);
            setScrollX(nextPage);
            currentPage = pageCoordinates.get(nextPage);
            isWasOnLeftEdge = isWasOnRightEdge = false;
            actionInScroll.run();
        }
    }



}